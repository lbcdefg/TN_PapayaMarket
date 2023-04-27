import java.awt.*
import java.awt.event.*
import java.sql.*
import javax.swing.*


class Chat :JFrame, ActionListener, Runnable, KeyListener{

    var postNO:String? = null         // 글번호 받아와서 여기에 들어가야함!
    var chatNO:String = ""
    var sellerId:String = ""
    var sellerNick:String = ""
    var buyerId:String = ""
    var buyerNick:String = ""
    var userId:String = ""
    var userState:String = ""
    var postTitle:String = ""

    var chMainP = JPanel()
    var chTopP = JPanel()
    var chBotP = JPanel()

    var chLogocgL = ImageIcon()

    var chTitleB = JButton()
    var chLogoL = JLabel()
    var chNickL = JLabel()
    var chBotImg = JLabel()

    var chChatTf = JTextField()
    var chChatfTa = JTextArea()

    var chLogoB = JButton()
    var chSendB = JButton()

    var con:Connection? = null
    var stmt:Statement? = null
    var rs:ResultSet? = null

    val chCCinsertsql = "insert into CHATCONTENT values(?,?,SYSDATE,?)"
    val chCOpenUsql = "update CHAT set CHATBUYEROUT='N', CHATSELLEROUT='N' where CHATNO=?"
    val chCExistsql = "select CHATNO from CHAT where POSTNO=? and CHATBUYERID=?"
    val chOpenchsql = "insert into CHAT values(CHAT_SEQ.nextval,?,?,'N','N',?)"
    val chChatnosql = "select CHATNO from CHAT where CHATBUYERID=? and POSTNO=?"
    val chPWIdTitlesql = "select USERID, POSTTITLE from POST where POSTNO=?"
    val chConListsql = "select c.CHATNO, c.CHATCON, c.CHATUDATE, u.USERNICK from CHATCONTENT c join USERINFO u on c.CHATSPEAKER = u.USERID where CHATNO=? order by CHATUDATE"
    var chSBuyerIdsql = "select CHATBUYERID from CHAT where CHATNO=?"
    var chSUserNicksql = "select USERNICK from USERINFO where USERID=? "

    var pstmtchConListsql:PreparedStatement? = null//채팅방 리스트 불러오기
    var pstmtchCCinsertsql:PreparedStatement? = null
    var pstmtchCOpenUsql:PreparedStatement? = null
    var pstmtchCExistsql:PreparedStatement? = null
    var pstmtchOpenchsql:PreparedStatement? = null
    var pstmtchChatnosql:PreparedStatement? = null
    var pstmtchPWIdTitlesql:PreparedStatement? = null
    var pstmtchMainBsql:PreparedStatement? = null
    var pstmtchSBuyerIdsql:PreparedStatement? = null
    var pstmtchSUserNicksql:PreparedStatement? = null

    var tower : Tower? = null

    constructor(tower: Tower, userId: String,postNO:String){//무조건 바이어
        this.tower = tower
        this.con = tower.con
        this.userId = userId
        this.postNO = postNO
        chatConnect()
        chatInit()
        chatSetUI()
    }

    constructor(tower: Tower, userId: String,postNO:String, chatNO:String){//바이어인지 셀러인지 확인하기 셀러라면 무조건 채팅no가 존재함
        this.tower = tower
        this.con = tower.con
        this.userId = userId
        this.postNO = postNO
        this.chatNO = chatNO
        println(this.chatNO)
        chatConnect()
        chatInit()
        chatSetUI()
    }

    fun chatInit(){
        var cp :Container = contentPane
        chMainP= JPanel()
        chMainP.setBounds(0,0,500,900)
        chMainP.background=Color.WHITE
        cp.add(chMainP)
        chMainP.layout=null

        chTopP = JPanel()
        chTopP.setBounds(0,0,500,100)
        chMainP.add(chTopP)
        chTopP.background= Color(240,255,255)
        chTopP.border=BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK)
        chTopP.layout=null

        chBotP = JPanel()
        chBotP.setBounds(0,772,500,100)
        chBotP.background= Color(240,255,255)
        chBotP.border=BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK)
        chMainP.add(chBotP)
        chBotP.layout=null

        chLogocgL = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")
        var chLogoImg: Image = chLogocgL.image
        var chLogoChangeImg:Image = chLogoImg.getScaledInstance(200,200, Image.SCALE_SMOOTH)
        var chLogoChangeIcon: ImageIcon = ImageIcon(chLogoChangeImg)

        chLogoB = JButton(chLogoChangeIcon)
        chLogoB.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 27)
        chLogoB.setBounds(0,0,200,100)
        chLogoB.addActionListener(this)
        chLogoB.isContentAreaFilled=false
        chLogoB.isFocusPainted=false
        chLogoB.isBorderPainted=false
        chLogoB.icon=chLogoChangeIcon
        chMainP.add(chLogoB)

        chLogoL = JLabel("")
        chLogoL.icon = chLogoChangeIcon
        chLogoL.setBounds(0,0,200,100)
        chTopP.add(chLogoL)

        val you:String = if (userState.equals("SELLER")){
            buyerNick
        } else{
            sellerNick
        }
        chNickL = JLabel("$you 님과의 대화")
        chNickL.setBounds(240,38,250,30)
        chNickL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        chNickL.horizontalAlignment = JLabel.CENTER
        chTopP.add(chNickL)

        chTitleB = JButton(postTitle)
        chTitleB.setBounds(20,125,455,35)
        chTitleB.addActionListener(this)
        chTitleB.isContentAreaFilled=false
        chTitleB.isFocusPainted=false
        chTitleB.isBorderPainted=false
        chTitleB.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 30)
        chMainP.add(chTitleB)

        chChatfTa = JTextArea()
        chChatfTa.setBounds(20,180,455,572)
        chChatfTa.lineWrap = true
        chChatfTa.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        var sp1 = JScrollPane(chChatfTa)
        sp1.setBounds(20,180,455,572)
        val pos:Int = chChatfTa.text.length
        chChatfTa.caretPosition = pos
        chChatfTa.isEditable = false
        chChatfTa.background=Color.white
        chMainP.add(sp1)


        chChatTf.setBounds(20,21,400,60)
        chChatTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        chChatTf.addKeyListener(this)
        //chChatTf.lineWrap = true
        chChatTf.isOpaque = true
        chBotP.add(chChatTf)

        var chSendcgL = ImageIcon(tower!!.path+"\\imgs\\send.png")
        var chSendImg: Image = chSendcgL.image
        var chSendChangeImg:Image = chSendImg.getScaledInstance(40,50, Image.SCALE_SMOOTH)
        var chSendChangeIcon: ImageIcon = ImageIcon(chSendChangeImg)

        chSendB = JButton(chSendChangeIcon)
        chSendB.setBounds(420,21,60,60)
        chSendB.font=Font("나눔스퀘어 네오 Bold", Font.PLAIN, 12)
        chSendB.addActionListener(this)
        chSendB.isContentAreaFilled=false
        chSendB.isFocusPainted=false
        chSendB.isBorderPainted=false
        chSendB.toolTipText = "채팅 보내기"
        chSendB.icon=chSendChangeIcon
        chBotP.add(chSendB)

        chBotImg = JLabel()
        chBotImg.icon=ImageIcon(tower!!.path+"imgs\\papayatree.png")
        chBotImg.setBounds(0,-20,500,125)
        chBotP.add(chBotImg)
    }

    fun chatSetUI() {
        title="TN Project Papaya Market"
        setSize(500, 900)
        var tk = Toolkit.getDefaultToolkit()
        var icon = tk.getImage(tower!!.path+"imgs\\파파야마켓.png")
        iconImage=icon
        isVisible=true
        setLocation(1350,100)
        isResizable=false
        defaultCloseOperation=JFrame.DISPOSE_ON_CLOSE
    }

    fun chatConnect(){
        try{
            stmt = con!!.createStatement()
            println("드라이버로딩 성공!")

            pstmtchPWIdTitlesql = con!!.prepareStatement(chPWIdTitlesql)
            //"select USERID from POST where POSTNO=?"
            pstmtchPWIdTitlesql!!.setString(1, postNO)
            rs=pstmtchPWIdTitlesql!!.executeQuery()

            rs!!.next()
            var postWriterID = rs!!.getString(1)
            postTitle = rs!!.getString(2)

            if(userId.equals(postWriterID)){
                userState = "SELLER"
                sellerId = userId
                getBuyerID()//셀러라면 무조건 채팅창이 열려있고 열려있는 그 채팅창의 바이어를 불러오기
            }else{
                userState = "BUYER"
                sellerId = postWriterID
                buyerId = userId
            }
            checkChat()

            getUserNick()

            Thread(this).start()
        }catch(e:Exception){
            println("드라이버 로딩실패: " +e)
        }catch(se: SQLException){
            println("커넥션 실패 / stmt 생성 실패: " + se)
        }
    }

    fun getBuyerID(){
        pstmtchSBuyerIdsql = con!!.prepareStatement(chSBuyerIdsql)
        pstmtchSBuyerIdsql!!.setString(1, chatNO)
        var rs = pstmtchSBuyerIdsql!!.executeQuery()
        rs.next()
        buyerId = rs.getString(1)
    }

    fun getUserNick(){
        pstmtchSUserNicksql = con!!.prepareStatement(chSUserNicksql)
        pstmtchSUserNicksql!!.setString(1, buyerId)
        var rs = pstmtchSUserNicksql!!.executeQuery()
        rs.next()
        buyerNick = rs.getString(1)

        pstmtchSUserNicksql!!.setString(1, sellerId)
        rs = pstmtchSUserNicksql!!.executeQuery()
        rs.next()
        sellerNick = rs.getString(1)
    }

    override fun run(){  //3초에 1번 대화내용 TextArea에 CHATCONTENT 테이블의 내용을 뿌려주는 쓰레드
        try {
            println("Thread에 들어왔습니다")

            println(chatNO)
            println(chatNO.equals(""))
            while(true){
                Thread.sleep(5000)
                if(!chatNO.equals("")){
                    println(chatNO)
                    break
                }
            }
            println("채팅방이 개설되었습니다 계속해서 대화를 불러옵니다.")

            val pstmtchConListsql = con!!.prepareStatement(chConListsql)
            pstmtchConListsql.setString(1, chatNO)

            while (true) {
                chChatfTa.text = null
                rs=pstmtchConListsql!!.executeQuery()
                while(rs!!.next()){
                    var speaker = rs!!.getString(4)
                    var chatContents = rs!!.getString(2)
                    var chatTime = rs!!.getString(3)
                    chChatfTa.append(speaker +"> "+ chatContents + "\n\n")
                    //chChatfTa.append(chatTime + "\n")           작성시간 나오게 해도되는데....가독성 매우떨어짐
                    val pos:Int = chChatfTa.text.length
                    chChatfTa.caretPosition = pos
                    chChatfTa.isOpaque = true
                }
                try {
                    Thread.sleep(3000)
                    chChatfTa.text=null
                } catch (ie: InterruptedException) {
                    println(ie)
                }
            }

        }catch(se:SQLException){
            println("출력실패(Thread) : $se")
        }finally{
            chatCloseAll()
        }
    }

    fun chatSend(){  //작성창(TextField)의 데이터를 CHATCONTENT 테이블로 전송
        try {
            println("send에 들어왔습니다")
            var chatContent = chChatTf.text
            if(!chatContent.equals("")) {
                val pstmtchCCinsertsql2 = con!!.prepareStatement(chCCinsertsql)
                //insert into CHATCONTENT values(?,?,SYSDATE,?)
                pstmtchCCinsertsql2.setString(1, chatNO)
                pstmtchCCinsertsql2.setString(2, chatContent)
                pstmtchCCinsertsql2.setString(3, userId)
                var i: Int = pstmtchCCinsertsql2!!.executeUpdate()
                if (i > 0) {
                    chChatTf.text = ""
                    pstmtchCOpenUsql = con!!.prepareStatement(chCOpenUsql)
                    pstmtchCOpenUsql!!.setString(1, chatNO)
                    pstmtchCOpenUsql!!.executeUpdate()
                } else {
                    println("CHATCONTENT 테이블에 insert 실패, 전송실패")
                }
            }
        }catch(se:SQLException){
            println("전송실패(send) : $se")
        }
    }

    fun checkChat():Boolean { //기존에 해당유저ID와 해당POSTNO로 채팅방 개설여부확인
        try {
            println("checkChat에 들어왔습니다")

            if(chatNO.equals("")) {
                val pstmtchCExistsql = con!!.prepareStatement(chCExistsql)
                //"select CHATNO from CHAT where POSTNO=? and CHATBUYERID=?"
                println(buyerId + ", " + postNO)
                pstmtchCExistsql.setString(1, postNO)
                pstmtchCExistsql.setString(2, buyerId)
                rs = pstmtchCExistsql!!.executeQuery()
                if (rs!!.next()) {
                    chatNO=rs!!.getString(1)
                }
            }
            if(chatNO.equals("")){
                return false
            }else{
                return true
            }
        }catch(se:SQLException){
            println("출력실패(checkChat) : $se")
            return false
        }
    }

    fun makeChat(){ //checkChat=false 임을 확인 하고 넘어왔으므로 해당 POSTNO에 USERID로 채팅방개설
        try {
            println("makeChat에 들어왔습니다")
            val pstmtchOpenchsql = con!!.prepareStatement(chOpenchsql)
            //"insert into CHAT values(CHAT_SEQ.nextval,?,?,'N','N',?)"
            pstmtchOpenchsql.setString(1, sellerId)
            pstmtchOpenchsql.setString(2, buyerId)
            pstmtchOpenchsql.setString(3, postNO)
            var i:Int = pstmtchOpenchsql!!.executeUpdate()
            if(i>0) {
                println("CHAT 테이블에 insert 성공")
            }else{
                println("CHAT 테이블에 insert 실패, 채팅방개설 실패. POST와 작성자를 확인하세요")
                return
            }
            val pstmtchChatnosql = con!!.prepareStatement(chChatnosql)
            //"select CHATNO from CHAT where CHATBUYERID=? and POSTNO=?"
            pstmtchChatnosql.setString(1, buyerId)
            pstmtchChatnosql.setString(2, postNO)
            rs=pstmtchChatnosql!!.executeQuery()
            while(rs!!.next()){
                chatNO = rs!!.getString(1)
                println(chatNO)
            }
        }catch(se:SQLException){
            println("전송실패(makeChat) : $se")
        }
    }

    fun chatCloseAll(){
        try {
            pstmtchConListsql?.close()
            pstmtchCCinsertsql?.close()
            pstmtchCOpenUsql?.close()
            pstmtchCExistsql?.close()
            pstmtchOpenchsql?.close()
            pstmtchChatnosql?.close()
            pstmtchPWIdTitlesql?.close()
            pstmtchMainBsql?.close()
            pstmtchSBuyerIdsql?.close()
            pstmtchSUserNicksql?.close()
        }catch(se:SQLException){}
    }

    override fun actionPerformed(e: ActionEvent) {
        var obj = e.source
        if (obj === chLogoB) {
            chatCloseAll()
            dispose()
            println("버튼이 눌렸어요")
        }
        if (obj === chSendB) { //작성창에서 전송버튼 클릭시 채팅 전송
            if(checkChat()){
                println(checkChat())
                chatSend()
            }else{
                makeChat()
                chatSend()
            }
        }
    }


    override fun keyPressed(e: KeyEvent){ //작성창에서 Enter키로 채팅 전송
        var keyCode:Int = e.keyCode
        when(keyCode){
            10 -> if(checkChat()){
                    chatSend()
                }else{
                    makeChat()
                    chatSend()
                }
        }
    }
    override fun keyReleased(e: KeyEvent) {}
    override fun keyTyped(e: KeyEvent) {}
}