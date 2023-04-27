import java.awt.Color
import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table
import java.awt.Container
import java.awt.Font
import java.awt.Image
import java.awt.event.*
import java.sql.*
import java.util.*
import javax.swing.*
import java.awt.*


class Myinfo:JFrame, ActionListener {

    var userId: String

    var userNick = String()
    var userP = String()
    var userTel = String()
    var userAddr = String()

    var sellerP = String()
    var buyerP = String()
    var dealSellerID = String()
    var dealBuyerID = String()

    var myinfoMainP = JPanel()
    var myinfoTopP = JPanel()
    var myinfoBotP = JPanel()

    var myinfoIdL = JLabel()
    var myinfoNickL = JLabel()
    var myinfoSeedL = JLabel()
    var myinfoMyseedL = JLabel()

    var myinfoTableT = JTable()
    var myinfoLogoI = ImageIcon()

    var myinfoLogoB = JButton()
    var myinfoEditB = JButton()
    var myinfoFavB = JButton()
    var myinfoDelB = JButton()
    var myinfoSeedB = JButton()

    var myinfoCon: Connection? = null
    var myinfoStmt: Statement? = null
    var myinfoRs: ResultSet? = null

    var myinfoMainsql =
        "select * from (select POSTNO 글번호, POSTST 상태 , POSTTITLE 글제목 , to_char(POSTREDATE, 'YY-MM-DD HH24:MI') 작성및거래일시 from POST where USERID=? and POSTDELETE='N' and POSTST != '판매완료' union select POSTNO 글번호, POSTST 상태, POSTTITLE 글제목, to_char(POSTREDATE,'YY-MM-DD HH24:MI') 작성및거래일시 from POST natural join DEAL where (USERID=? or DEALBUYERID=?) and DEALDELETE='N' and POSTDELETE='N' ) order by 작성및거래일시 desc"
    var myinfoUsersql = "select USERNICK,nvl(USERP,0),USERTEL,USERADDR from USERINFO where USERID=?"
    var myinfoAvgseedsql =
        "update USERINFO set USERP=(select sum(A)/ sum(B) from (select sum(SELLERP) A ,count(SELLERP) B from DEAL where DEALSELLERID=? union select sum(BUYERP) A ,count(BUYERP) B from DEAL where DEALBUYERID=?)) where USERID=?"
    var myinfoDealpsql = "update DEAL set SELLERP =? where POSTNO=?"
    var myinfoDealbpsql = "update DEAL set BUYERP = ? where POSTNO=?"
    var myinfoDealdelsql = "update DEAL set DEALDELETE='Y' where POSTNO=?"
    var myinfoDealdelChecksql = "select nvl(SELLERP,0), nvl(BUYERP,0) from DEAL where POSTNO=?"
    var myinfoPostdelsql = "update POST set POSTDELETE='Y' where POSTNO=?"
    var myinfoItemsql = "select nvl(SELLERP,0), nvl(BUYERP,0), DEALSELLERID, DEALBUYERID from DEAL where POSTNO=?"

    var myinfoMainpstmt: PreparedStatement? = null
    var myinfoUserpstmt: PreparedStatement? = null
    var myinfoAvgseedpstmt: PreparedStatement? = null
    var myinfoDealppstmt: PreparedStatement? = null   //sql4+5 재활용함
    var myinfoDeletepstmt: PreparedStatement? = null   //sql 6+7 재활용함
    var myinfoItempstmt: PreparedStatement? = null
    lateinit var myinfoDealSeedpstmt: PreparedStatement

    var tower : Tower? = null

    constructor(tower:Tower, userId: String) {
        this.tower = tower
        this.userId = userId
        this.userNick = userNick
        this.userTel = userTel
        this.userAddr = userAddr
        connect()
        initialize()
        setUI()
    }

    fun initialize() {
        myinfoGet1st(userId)
        var cp: Container = contentPane
        myinfoMainP = JPanel()
        myinfoMainP.setBounds(0, 0, 500, 900)
        myinfoMainP.background = Color.WHITE
        cp.add(myinfoMainP)
        myinfoMainP.layout = null

        myinfoTopP = JPanel()
        myinfoTopP.setBounds(0, 0, 500, 100)
        myinfoMainP.add(myinfoTopP)
        myinfoTopP.background = Color(240,255,255)
        myinfoTopP.border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)
        myinfoTopP.layout = null

        myinfoBotP = JPanel()
        myinfoBotP.setBounds(0, 772, 500, 100)
        myinfoBotP.background = Color(240,255,255)
        myinfoBotP.border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)
        myinfoMainP.add(myinfoBotP)
        myinfoBotP.layout = null

        myinfoLogoI = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")
        var myinfologoImg: Image = myinfoLogoI.image
        var myinfologoChangeImg: Image = myinfologoImg.getScaledInstance(200, 200, Image.SCALE_SMOOTH)
        var myinfologoChangeIcon = ImageIcon(myinfologoChangeImg)

        myinfoLogoB = JButton()
        myinfoLogoB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 23)
        myinfoLogoB.setBounds(0, 0, 200, 100)
        myinfoLogoB.addActionListener(this)
        myinfoLogoB.isContentAreaFilled = false
        myinfoLogoB.isFocusPainted = false
        myinfoLogoB.isBorderPainted = false
        myinfoLogoB.toolTipText = "메인으로 돌아가기"
        myinfoLogoB.icon = myinfologoChangeIcon
        myinfoTopP.add(myinfoLogoB)

        myinfoIdL = JLabel("ID: " + userId)
        myinfoIdL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        myinfoIdL.setBounds(20, 110, 240, 50)
        myinfoMainP.add(myinfoIdL)

        myinfoNickL = JLabel("NICK: "+userNick)
        myinfoNickL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        myinfoNickL.setBounds(20, 170, 240, 50)
        myinfoMainP.add(myinfoNickL)

        var myinfoLogoCg = ImageIcon(tower!!.path+"imgs\\씨앗.png")
        var myinfoLogoImg: Image = myinfoLogoCg.image
        var myinfoLogoChageImg:Image = myinfoLogoImg.getScaledInstance(80,80, Image.SCALE_SMOOTH)
        var myinfoLogoChageIcon: ImageIcon = ImageIcon(myinfoLogoChageImg)

        myinfoSeedL = JLabel()
        myinfoSeedL.icon=myinfoLogoChageIcon
        myinfoSeedL.setBounds(260, 130, 80, 80)
        myinfoMainP.add(myinfoSeedL)

        myinfoMyseedL = JLabel(userP)
        myinfoMyseedL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        myinfoMyseedL.setBounds(370, 135, 100, 70)
        myinfoMyseedL.background = Color.yellow
        myinfoMainP.add(myinfoMyseedL)

        var myinfoEditCg = ImageIcon(tower!!.path+"imgs\\edit.png")
        var myinfoEditImg: Image = myinfoEditCg.image
        var myinfoEditChageImg:Image = myinfoEditImg.getScaledInstance(60,60, Image.SCALE_SMOOTH)
        var myinfoEditChageIcon: ImageIcon = ImageIcon(myinfoEditChageImg)

        myinfoEditB = JButton()
        myinfoEditB.icon=myinfoEditChageIcon
        myinfoEditB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        myinfoEditB.setBounds(0, 0, 125, 100)
        myinfoEditB.addActionListener(this)
        myinfoEditB.isContentAreaFilled = false
        myinfoEditB.isFocusPainted = false
        myinfoEditB.toolTipText = "내정보 변경"
        myinfoBotP.add(myinfoEditB)

        var myinfoPostCg = ImageIcon(tower!!.path+"imgs\\postinfo.png")
        var myinfoPostImg: Image = myinfoPostCg.image
        var myinfoPostChageImg:Image = myinfoPostImg.getScaledInstance(60,60, Image.SCALE_SMOOTH)
        var myinfoPostChageIcon: ImageIcon = ImageIcon(myinfoPostChageImg)

        myinfoFavB = JButton()
        myinfoFavB.icon=myinfoPostChageIcon
        myinfoFavB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        myinfoFavB.setBounds(125, 0, 125, 100)
        myinfoFavB.addActionListener(this)
        myinfoFavB.isContentAreaFilled = false
        myinfoFavB.isFocusPainted = false
        myinfoFavB.toolTipText = "글 상세보기"
        myinfoBotP.add(myinfoFavB)

        var myinfoDelCg = ImageIcon(tower!!.path+"imgs\\poinDelete.png")
        var myinfoDelImg: Image = myinfoDelCg.image
        var myinfoDelChageImg:Image = myinfoDelImg.getScaledInstance(60,60, Image.SCALE_SMOOTH)
        var myinfoDelChageIcon: ImageIcon = ImageIcon(myinfoDelChageImg)

        myinfoDelB = JButton()
        myinfoDelB.icon=myinfoDelChageIcon
        myinfoDelB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        myinfoDelB.setBounds(250, 0, 125, 100)
        myinfoDelB.addActionListener(this)
        myinfoDelB.isContentAreaFilled = false
        myinfoDelB.isFocusPainted = false
        myinfoDelB.toolTipText = "목록에서 제거"
        myinfoBotP.add(myinfoDelB)

        var myinfoSeedCg = ImageIcon(tower!!.path+"imgs\\givelove.png")
        var myinfoSeedImg: Image = myinfoSeedCg.image
        var myinfoSeedChageImg:Image = myinfoSeedImg.getScaledInstance(60,60, Image.SCALE_SMOOTH)
        var myinfoSeedChageIcon = ImageIcon(myinfoSeedChageImg)

        myinfoSeedB = JButton()
        myinfoSeedB.icon=myinfoSeedChageIcon
        myinfoSeedB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        myinfoSeedB.setBounds(375, 0, 125, 100)
        myinfoSeedB.addActionListener(this)
        myinfoSeedB.isContentAreaFilled = false
        myinfoSeedB.isFocusPainted = false
        myinfoSeedB.toolTipText = "씨앗 주기"
        myinfoBotP.add(myinfoSeedB)

        myinfoTableT = JTable(getRowData(), getColumn())
        var sp = JScrollPane(myinfoTableT)
        sp.setBounds(20, 230, 455, 522)
        myinfoMainP.add(sp)
        myinfoTableT.font = (Font("나눔스퀘어 네오 Bold", Font.BOLD, 20))
        myinfoTableT.setRowHeight(40)
        myinfoTableT.getColumn("글번호").width = 0
        myinfoTableT.getColumn("글번호").minWidth = 0
        myinfoTableT.getColumn("글번호").maxWidth = 0
        myinfoTableT.getColumn("상태").width = 90
        myinfoTableT.getColumn("상태").minWidth = 90
        myinfoTableT.getColumn("상태").maxWidth = 90
        myinfoTableT.getColumn("작성및거래일시").width = 130
        myinfoTableT.getColumn("작성및거래일시").minWidth = 130
        myinfoTableT.getColumn("작성및거래일시").maxWidth = 130

        var myinfoBotImg = JLabel()
        myinfoBotImg.icon=ImageIcon(tower!!.path+"imgs\\papayatree.png")
        myinfoBotImg.setBounds(0,-20,500,125)
        myinfoBotP.add(myinfoBotImg)
    }

    fun selectItem(vIndex: Int): String? {
        var tableitem = Vector<String?>()
        var selRow: Int = myinfoTableT.selectedRow
        try {
            var myinfoItempstmt = myinfoCon!!.prepareStatement(myinfoItemsql)
            //select SELLERP, BUYERP, DEALSELLERID, DEALBUYERID from DEAL where POSTNO=?
            myinfoItempstmt!!.setString(1, "" + myinfoTableT.getValueAt(selRow, 0))
            myinfoRs = myinfoItempstmt!!.executeQuery()
            while (myinfoRs!!.next()) {
                sellerP = myinfoRs!!.getString(1)
                buyerP = myinfoRs!!.getString(2)
                dealSellerID = myinfoRs!!.getString(3)
                dealBuyerID = myinfoRs!!.getString(4)
                tableitem.add(sellerP)
                tableitem.add(buyerP)
                tableitem.add(dealSellerID)
                tableitem.add(dealBuyerID)
            }
        } catch (se: SQLException) {
            println("selectItem 실패 :$se")
        }
        var reqval = tableitem.get(vIndex)
        return reqval
    }

    fun connect() {
        try {
            myinfoCon = tower!!.con!!
            myinfoStmt = myinfoCon!!.createStatement()
            println("드라이버로딩 성공!")
            //getColumn();
            myinfoDealSeedpstmt = myinfoCon!!.prepareStatement(myinfoDealdelChecksql)
        } catch (e: Exception) {
            println("드라이버 로딩실패: " + e)
        } catch (se: SQLException) {
            println("커넥션 실패 / stmt 생성 실패: " + se)
        }
    }

    // JTable의 columnName을 만드는 벡터 구하는 Function
    fun getColumn(): Vector<String> {
        var columnNames = Vector<String>()
        try {
            var myinfoMainpstmt = myinfoCon!!.prepareStatement(myinfoMainsql)
            myinfoMainpstmt.setString(1, userId)
            myinfoMainpstmt.setString(2, userId)
            myinfoMainpstmt.setString(3, userId)
            myinfoRs = myinfoMainpstmt?.executeQuery()
            var rsmd: ResultSetMetaData? = myinfoRs?.getMetaData()
            var cc: Int = rsmd!!.columnCount
            println("칼럼 갯수: " + cc)
            for (i in 1..cc) {
                var cn = rsmd!!.getColumnName(i)
                columnNames.add(cn)
            }
            //getRowData()
        } catch (se: SQLException) {
            println("SQLException: $se")
        } finally {
            try {
                if (myinfoRs != null) myinfoRs!!.close()
            } catch (se: SQLException) {
            }
        }
        return columnNames
    }

    // JTable의 rowData를 만드는 벡터 구하는 Function
    fun getRowData(): Vector<Vector<Any>> {
        val rowData = Vector<Vector<Any>>()
        try {
            var myinfoMainpstmt = myinfoCon!!.prepareStatement(myinfoMainsql)
            myinfoMainpstmt.setString(1, userId)
            myinfoMainpstmt.setString(2, userId)
            myinfoMainpstmt.setString(3, userId)
            myinfoRs = myinfoMainpstmt?.executeQuery()
            while (myinfoRs!!.next()) {
                println("&&&&&&&&&&&&&&&&&&&&&")
                var rsmd = myinfoRs!!.metaData
                var numberOfColumns = rsmd!!.columnCount
                var temp = Vector<Any>()
                for (i in 1..numberOfColumns) {
                    var data = myinfoRs!!.getString(i)
                    if(i == numberOfColumns){
                        data = data.substring(0, data.indexOf(" "))
                    }
                    println(myinfoRs!!.getString(i))
                    temp.add(data)
                }
                rowData.add(temp)
            }
            //dftmodel.setDataVector(rowData, columnNames)
        } catch (se: SQLException) {
            println(se)
        } finally {
            try {
                if (myinfoRs != null) myinfoRs!!.close()
            } catch (se: SQLException) {
            }
        }
        return rowData
    }

    fun setUI() {
        title = "TN Project Papaya Market"
        setSize(500, 900)
        var tk = Toolkit.getDefaultToolkit()
        var icon = tk.getImage(tower!!.path+"imgs\\파파야마켓.png")
        iconImage=icon
        isVisible = true
        setLocationRelativeTo(null)
        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    // 접속자의 닉네임과 씨앗점수를 가져오는 Function(아이디)
    fun myinfoGet1st(ID: String) {
        try {
            var myinfoUserpstmt = myinfoCon!!.prepareStatement(myinfoUsersql)
            myinfoUserpstmt.setString(1, ID)
            myinfoRs = myinfoUserpstmt.executeQuery()
            while (myinfoRs!!.next()) {
                userNick = myinfoRs!!.getString(1)
                userP = myinfoRs!!.getString(2)
                userTel = myinfoRs!!.getString(3)
                userAddr = myinfoRs!!.getString(4)
                //println("userNick: $userNick , userP: $userP")
            }
        } catch (se: SQLException) {
            println("(myinfoGet1st)정보조회 실패: $se")
        }
    }

    // 입력된 아이디에게 입력된 개수의 씨앗을 주는 Function(아이디, 씨앗개수, SQL문)    SQL문은 셀러포인트,바이어포인트 두 종류 업데이트로 나뉨
    fun myinfoGiveSeed(ID: String, choosenSeedPoint: Int, SQL: String) {
        var selRow: Int = myinfoTableT.selectedRow
        try {
            println("ID 는 $ID, 씨앗점수는 $choosenSeedPoint")
            var myinfoDealpsqlpstmt = myinfoCon!!.prepareStatement(SQL)
            //"update DEAL set SELLERP =? where POSTNO=?"
            myinfoDealpsqlpstmt.setInt(1, choosenSeedPoint)
            myinfoDealpsqlpstmt.setString(2, "" + myinfoTableT.getValueAt(selRow, 0))
            var i: Int = myinfoDealpsqlpstmt.executeUpdate()
            if (i > 0) {
                println("$ID 님에게 씨앗 $choosenSeedPoint 개 주기 성공!")
            }
        } catch (se: SQLException) {
            println("(myinfoGiveSeed)씨앗점수 주기 실패: $se")
        }
    }

    // USERINFO 테이블의 평균합산씨앗점수를 업데이트 하는 Funtion(아이디)      myinfoGiveSeed Function 이후 반드시 후행되어야함!
    fun myinfoSetSeed(ID: String) {
        try {
            var myinfoAvgseedpstmt = myinfoCon!!.prepareStatement(myinfoAvgseedsql)
            myinfoAvgseedpstmt.setString(1, ID)
            myinfoAvgseedpstmt.setString(2, ID)
            myinfoAvgseedpstmt.setString(3, ID)
            var i: Int = myinfoAvgseedpstmt!!.executeUpdate()
            if (i > 0) {
                println("씨앗점수 업데이트 완료")
            }
        } catch (se: SQLException) {
            println("(myinfoSetSeed)씨앗점수 업데이트 실패: $se")
        }
    }

    // 목록에서 제거시 DELETE 컬럼의 데이터를 'N'에서 'Y'로 바꾸는 Function(글번호)    SQL문은 POST와 DEAL 두 종류 업데이트로 나뉨
    fun myinfoDelete(delpostNO: String, SQL: String) {
        try {
            var myinfoDeletepstmt = myinfoCon!!.prepareStatement(SQL)
            myinfoDeletepstmt.setString(1, delpostNO)
            var i: Int = myinfoDeletepstmt.executeUpdate()
            if (i > 0) {
                println("목록에서 제거 완료")
            }
        } catch (se: SQLException) {
            println("목록에서 제거 실패: $se")
        }
    }

    fun myinfoDeleteSeedCheck(myinfoPostNO: String):Boolean {
        var myInfoBoolean = true
        myinfoDealSeedpstmt.setString(1,myinfoPostNO)
        var myinfoRS =myinfoDealSeedpstmt.executeQuery()
        while(myinfoRS.next()){
            var sellerP = myinfoRS.getInt(1)
            var BuyerP = myinfoRS.getInt(2)
            println(sellerP)
            println(BuyerP)
            if((BuyerP != 0) && (sellerP != 0)){
                myInfoBoolean = false
            }
        }
        return myInfoBoolean
    }

    fun myinfoCloseAll(){
        try {
            myinfoMainpstmt?.close()
            myinfoUserpstmt?.close()
            myinfoAvgseedpstmt?.close()
            myinfoDealppstmt?.close()
            myinfoDeletepstmt?.close()
            myinfoItempstmt?.close()
        }catch(se:SQLException){}

    }

    override fun actionPerformed(e: ActionEvent) {
        var obj = e.source
        var selRow: Int = myinfoTableT.selectedRow
        if (obj === myinfoLogoB) {
            myinfoCloseAll()
            dispose()
            Main(tower!!, userId, "All")
        }
        if (obj === myinfoEditB) {
            myinfoCloseAll()
            dispose()
            Editinfo(tower!!, userId,userNick,userTel,userAddr)
        }
        if (obj === myinfoFavB) {
            if (selRow == -1) {
                JOptionPane.showMessageDialog(
                    null, "열람 할 대상을 선택하세요", "상세보기",
                    JOptionPane.ERROR_MESSAGE
                )
            }else {
                myinfoCloseAll()
                dispose()
                PostInfo(tower!!, userId, Integer.parseInt("" + myinfoTableT.getValueAt(selRow, 0)))
            }
        }
        if (obj === myinfoDelB) {
            if (selRow == -1) {
                JOptionPane.showMessageDialog(
                    null, "목록에서 제거할 대상을 선택하세요", "목록에서 제거",
                    JOptionPane.ERROR_MESSAGE
                )
            }else if(myinfoTableT.getValueAt(selRow, 1).equals("판매완료") && myinfoDeleteSeedCheck("" + myinfoTableT.getValueAt(selRow, 0))) {
                JOptionPane.showMessageDialog(null, "평점교환이 완료되지 않았습니다", "제거불가", JOptionPane.ERROR_MESSAGE)
            }else {
                var deleteoptionresult: Int = JOptionPane.showConfirmDialog(
                    null, "정말 제거하시겠습니까?", "목록에서 제거",
                    JOptionPane.YES_NO_OPTION
                )
                if (deleteoptionresult == JOptionPane.YES_OPTION) {
                    if (myinfoTableT.getValueAt(selRow, 1).equals("판매완료")) {  //판매완료면 DEAL에서 delete컬럼 데이터 'Y'로 업데이트
                        myinfoDelete("" + myinfoTableT.getValueAt(selRow, 0), myinfoDealdelsql)
                        //"update DEAL set DEALDELETE='Y' where POSTNO=?"
                        myinfoCloseAll()
                        dispose()
                        Myinfo(tower!!, userId)
                    } else {
                        myinfoDelete(
                            "" + myinfoTableT.getValueAt(selRow, 0),
                            myinfoPostdelsql
                        )    //판매중 혹은 예약중이면 POST에서 delete컬럼 데이터 'Y'로 업데이트
                        //"update POST set POSTDELETE='Y' where POSTNO=?"
                        myinfoCloseAll()
                        dispose()
                        Myinfo(tower!!, userId)
                    }
                }
            }
        }
        if (obj === myinfoSeedB) {
            val seedOptions = arrayOf<Any>(
                1, 2, 3, 4, 5
            )
            if (selRow == -1) {
                JOptionPane.showMessageDialog(
                    null, "목록에서 씨앗을 줄 대상을 선택하세요", "씨앗주기",
                    JOptionPane.ERROR_MESSAGE
                )
            } else {
                if (myinfoTableT.getValueAt(selRow, 1).equals("판매완료")) {
                    val choosenSeedPoint = JOptionPane.showOptionDialog(
                        null, "상대방에게 씨앗을 주세요!", "TN Project Papaya Market",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, seedOptions, seedOptions[0]
                    ) + 1
                    println("선택한씨앗점수: $choosenSeedPoint")

                    when (userId) {
                        selectItem(2) -> if (selectItem(1).equals("0")) {
                            println("어디까지왔니1111111111")
                            //"update DEAL set BUYERP = ? where POSTNO=?"
                            myinfoGiveSeed("" + selectItem(3), choosenSeedPoint, myinfoDealbpsql)
                            myinfoSetSeed("" + selectItem(3))
                            myinfoCloseAll()
                            dispose()
                            Myinfo(tower!!, userId)
                        } else {
                            println("어디까지왔니2222222")
                            JOptionPane.showMessageDialog(
                                null, "이미 씨앗을 준 구매자입니다!",
                                "TN Project Papaya Market", JOptionPane.ERROR_MESSAGE
                            )
                        }

                        selectItem(3) -> if (selectItem(0).equals("0")) {
                            println("어디까지왔니3333333333")
                            //"update DEAL set SELLERRP = ? where POSTNO=?"
                            myinfoGiveSeed("" + selectItem(2), choosenSeedPoint, myinfoDealpsql)
                            myinfoSetSeed("" + selectItem(2))
                            myinfoCloseAll()
                            dispose()
                            Myinfo(tower!!, userId)
                        } else {
                            println("어디까지왔니444444444")
                            JOptionPane.showMessageDialog(
                                null, "이미 씨앗을 준 판매자입니다!",
                                "TN Project Papaya Market", JOptionPane.ERROR_MESSAGE
                            )
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        null, "판매완료시에만 씨앗을 줄 수 있습니다!",
                        "TN Project Papaya Market", JOptionPane.ERROR_MESSAGE
                    )
                }
            }
        }
    }
}