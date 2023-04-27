import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*
import javax.swing.*


class ChatList : JFrame, ActionListener {
    var userId: String

    var chatLcp = contentPane
    var chatLMainP = JPanel()
    var chatLUpP = JPanel()
    var chatLDownP = newPostInfoJpanel()

    //위쪽 구성요소
    var chatLLogoB = JButton()
    var chatLTitleL = JLabel()

    //가운데 구성요소
    var chatLChatlistL = JList<Vector<Any>>()

    //아래쪽 구성요소
    var chatLUserNickC = JComboBox<String>()
    var chatLDeleteB = JButton()

    // 리스트 눌렀을때 가져올 데이터 값 저장소
    lateinit var chatLSelectedData1: Any
    lateinit var chatLSelectedData2: Any

    //SQL 불러올때 필요한 것들
    lateinit var con: Connection
    lateinit var chatLpstmt0: PreparedStatement  // 채팅 관련 전체 내용 다 받기
    var chatLSql = "select * from CHAT natural join (select Max(CHATUDATE)CHATUDATE, CHATNO from CHATCONTENT group by CHATNO)  order by CHATUDATE desc"
    var checkChatNo:Int = 0
    lateinit var checkChatSellerId:Any
    lateinit var checkChatBuyerId:Any
    lateinit var checkChatSellerOutId:Any
    lateinit var checkChatBuyerOutId:Any
    lateinit var checkChatPostNo:Any

    lateinit var chatLpstmt1: PreparedStatement  // ID로 닉네임 찾기
    var chatLGetNickSql = "select USERNICK from USERINFO where USERID=?"
    var chatLTreeSetForJCombo:TreeSet<String> = TreeSet()

    lateinit var chatLpstmt2: PreparedStatement  // 채팅 리스트 만들기용
    var chatLChatConSql = "select * from (select POSTTITLE, CHATCON, to_char(CHATUDATE, 'YY-MM-DD AM HH:MI'), CHATSPEAKER, c.POSTNO" +
            " from CHAT c join CHATCONTENT cc on c.CHATNO=cc.CHATNO join POST p on c.POSTNO=p.POSTNO" +
            " where cc.CHATNO=? order by CHATUDATE desc) where ROWNUM=1"
    var chatLDataList =DefaultListModel<Vector<Any>>()

    lateinit var chatLpstmt3: PreparedStatement  // 닉으로 ID찾기
    var chatLGetIDSql = "select USERID from USERINFO where USERNICK=?"

    lateinit var chatLpstmt4: PreparedStatement  // 본인 ID와 상대방ID로 CHATNO 다 찾기
    var chatLGetChatNoSql = "select CHATNO from CHAT where CHATSELLERID=? and CHATBUYERID=? and CHATSELLEROUT='N' union select CHATNO from CHAT where CHATSELLERID=? and CHATBUYERID=? and CHATBUYEROUT='N'"

    lateinit var chatLpstmt5: PreparedStatement  // CHATNO로 찾아서 CHATOUT 불러오기
    var chatLSellBuySql = "select CHATSELLERID, CHATBUYERID, CHATSELLEROUT, CHATBUYEROUT from CHAT where CHATNO=?"

    lateinit var chatLpstmt6: PreparedStatement  // CHATOUT 세팅 유저가 SELLER일때
    var chatLSetSellerOutSql = "update CHAT set CHATSELLEROUT='Y' where CHATNO=?"

    lateinit var chatLpstmt7: PreparedStatement  // CHATOUT 세팅 유저가 BUYER일때
    var chatLSetBuyerOutSql = "update CHAT set CHATBUYEROUT='Y' where CHATNO=?"

    var tower : Tower? = null

    // SQL연결
    fun chatListKdbc() {
        chatLpstmt0 = con.prepareStatement(chatLSql)
        chatLpstmt1 = con.prepareStatement(chatLGetNickSql)
        chatLpstmt2 = con.prepareStatement(chatLChatConSql)
        chatLpstmt3 = con.prepareStatement(chatLGetIDSql)
        chatLpstmt4 = con.prepareStatement(chatLGetChatNoSql)
        chatLpstmt5 = con.prepareStatement(chatLSellBuySql)
        chatLpstmt6 = con.prepareStatement(chatLSetSellerOutSql)
        chatLpstmt7 = con.prepareStatement(chatLSetBuyerOutSql)

        println("드라이버 로딩 성공")   // 검증!!!
    }

    //채팅 컬럼 전체 받기
    fun chatLMy(userId:String){
        var chatLRsP = chatLpstmt0.executeQuery()

        while(chatLRsP.next()) {
            checkChatNo = chatLRsP.getInt(1)
            checkChatSellerId = chatLRsP.getObject(2)
            checkChatBuyerId = chatLRsP.getObject(3)
            checkChatSellerOutId = chatLRsP.getObject(4)
            checkChatBuyerOutId = chatLRsP.getObject(5)
            checkChatPostNo = chatLRsP.getObject(6)
            if(checkChatSellerId.equals(userId)){  // 내 글일때 상대방은 구매자
                if(checkChatSellerOutId.toString().equals("N",ignoreCase = true)) {
                    chatLTreeSetForJCombo.add("${checkChatBuyerId}")
                    chatLGetList(checkChatNo, checkChatBuyerId.toString())
                }else if(checkChatSellerOutId.toString().equals("Y",ignoreCase = true)){
                    chatLUserNickC.removeItem(chatLGetListforUserNick(checkChatBuyerId.toString()))
                }
            }else if(checkChatBuyerId.equals(userId)){  // 남의 글일때 상대방은 판매자
                if(checkChatBuyerOutId.toString().equals("N",ignoreCase = true)) {
                    chatLTreeSetForJCombo.add("${checkChatSellerId}")
                    chatLGetList(checkChatNo, checkChatSellerId.toString())
                }else if(checkChatBuyerOutId.toString().equals("Y",ignoreCase = true)){
                    chatLUserNickC.removeItem(chatLGetListforUserNick(checkChatSellerId.toString()))
                }
            }
        }
    }


    // 리스트용 데이터 삽입 기능 -- for List 1
    fun chatLGetList(chatLChatGetNo:Int, chatLOtherUser:String){
        if(chatLChatGetNo>0) {
            var chatLRowdata = Vector<Any>()
            chatLRowdata.add(chatLChatGetNo)
            chatLRowdata.add(chatLGetListforUserNick(chatLOtherUser))
            chatLpstmt2.setInt(1, chatLChatGetNo)
            var chatLRsP = chatLpstmt2.executeQuery()
            while(chatLRsP.next()) {
                chatLRowdata.add(chatLRsP.getObject(1))
                chatLRowdata.add(chatLRsP.getObject(2))
                chatLRowdata.add(chatLRsP.getObject(3))
                chatLRowdata.add(chatLGetListforUserNick(chatLRsP.getObject(4).toString()))
                chatLRowdata.add(chatLRsP.getObject(5))
            }
            chatLDataList.addElement(chatLRowdata)
        }
    }

    // 닉 찾기 기능 -- for List 2
    fun chatLGetListforUserNick(chatLOtherUser:String):String {
        var chatLThrowUserNick = ""
        chatLpstmt1.setString(1,chatLOtherUser)
        var chatLRsP = chatLpstmt1.executeQuery()
        while(chatLRsP.next()) {
            chatLThrowUserNick = chatLRsP.getString(1)
        }
        return chatLThrowUserNick
    }

    //유저 닉 찾아서 콤보박스 넣기 -> TreeSet 이용하여 중복제거 및 오름차순으로 넣음
    fun chatUserNick(){
        var chatLTreesetI: Iterator<String> = chatLTreeSetForJCombo.iterator()
        chatLUserNickC.addItem(chatLTreesetI.next())    //" 전체" 먼저 넣기
        while(chatLTreesetI.hasNext()) {
            chatLpstmt1.setString(1,chatLTreesetI.next())
            var chatLRsP = chatLpstmt1.executeQuery()
            while(chatLRsP.next()) {
                chatLUserNickC.addItem(chatLRsP.getString(1))
            }
        }
    }

    // ChatList생성자
    constructor(tower:Tower, userId:String){
        this.tower = tower
        this.con = tower.con!!
        this.userId = userId
        chatListKdbc()
        chatLTreeSetForJCombo.add(" 전체")    // Default로 콤보박스에 전체 넣어놓기(제일 위에 오도롣 맨 앞 " ")
        chatLMy(userId)
        chatUserNick()
        chatLTreeSetForJCombo.clear()
        chatLInin()
        ChatLSetUI()
    }

    // UI 구성 부분
    fun chatLInin(){
        chatLcp.add(chatLMainP)
        chatLMainP.add(chatLUpP)
        chatLMainP.add(chatLDownP)
        chatLMainP.layout = null
        chatLMainP.background = Color.WHITE


        //위쪽 판넬
        chatLUpP.layout = null
        chatLUpP.setBounds(0,0,500,100)
        chatLUpP.background= Color(240,255,255)
        chatLUpP.border = BorderFactory.createMatteBorder(0,0,2,0, Color.lightGray)
        var chatLLogoRowIcon = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")
        var chatLLogoImage = chatLLogoRowIcon.image.getScaledInstance(200,200, Image.SCALE_SMOOTH)  // 로고이미지 작업
        var chatLLogoIcon = ImageIcon(chatLLogoImage)

        chatLLogoB.icon=chatLLogoIcon
        chatLLogoB.setBounds(0,0,200,100)
        chatLLogoB.isFocusPainted=false
        chatLLogoB.isContentAreaFilled=false
        chatLLogoB.isBorderPainted=false
        chatLLogoB.toolTipText = "메인으로 돌아가기"
        chatLLogoB.addActionListener(this)
        chatLUpP.add(chatLLogoB)

        var chatLTitleRowIcon = ImageIcon(tower!!.path+"imgs\\chatLcloud.png")
        var chatLTitleImage = chatLTitleRowIcon.image.getScaledInstance(200,100, Image.SCALE_SMOOTH)  // 타이틀 작업
        var chatLTitleIcon = ImageIcon(chatLTitleImage)
        chatLTitleL = JLabel(chatLTitleIcon)
        chatLTitleL.setBounds(280,0,200,100)
        chatLUpP.add(chatLTitleL)

        //중앙 판넬
        chatLChatlistL = JList(chatLDataList)
        chatLChatlistL.setBounds(0,100,515,672)
        chatLChatlistL.fixedCellHeight = 70
        chatLChatlistL.setCellRenderer(chatLLabelCellRenderer())
        chatLChatlistL.addMouseListener(mouseAdaptor())
        val sp = JScrollPane(chatLChatlistL)
        sp.setBounds(0,100,515,672)
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatLMainP.add(sp)


        //아래쪽 판넬
        chatLDownP.layout = null
        chatLDownP.border = BorderFactory.createMatteBorder(2,0,0,0, Color.lightGray)
        chatLDownP.setBounds(0,772,500,100)

        chatLUserNickC.setBounds(5,20,200,50)  // 상태창 콤보박스
        var listRenderer2 = DefaultListCellRenderer()    // 콤보박스 Item 센터 맞추기
        listRenderer2.horizontalAlignment = CENTER_ALIGNMENT.toInt()
        chatLUserNickC.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
        chatLUserNickC.foreground = Color.BLACK.brighter()
        chatLUserNickC.background = Color.WHITE
        chatLUserNickC.renderer = listRenderer2
        chatLUserNickC.addActionListener(this)
        chatLDownP.add(chatLUserNickC)


        chatLDeleteB.setBounds(400, 0, 100, 100)
        var chatLDeleteRowIcon = ImageIcon(tower!!.path+"imgs\\PoinDelete.png")
        var chatLDeleteImage = chatLDeleteRowIcon.image.getScaledInstance(70,70, Image.SCALE_SMOOTH)  // 로고이미지 작업
        var chatLDeleteIcon = ImageIcon(chatLDeleteImage)
        chatLDeleteB.icon = chatLDeleteIcon
        chatLDeleteB.isContentAreaFilled=false
        chatLDeleteB.isFocusPainted=false
        chatLDeleteB.toolTipText = "채팅방에서 나가기"
        chatLDeleteB.addActionListener(this)
        chatLDownP.add(chatLDeleteB)
    }

    fun ChatLSetUI() {
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

    //카테고리 고른거에 따라서 테이블에 올리기 -- for Category1
    fun charLcategorySetUI(chatLCategoryItem: String){
        chatLDataList.clear()
        if(chatLCategoryItem.equals(" 전체")){
            chatLMy(userId)
        }else {
            var chatLOtherUserId = chatLGetListInUserID(chatLCategoryItem)
            chatLGetListInChatNo(chatLOtherUserId)
        }
    }

    //유저 아이디 찾기 -- for Category2
    fun chatLGetListInUserID(chatLOtherUser:String):String {
        var chatLThrowUserID = ""
        chatLpstmt3.setString(1,chatLOtherUser)
        var chatLRsP = chatLpstmt3.executeQuery()
        while(chatLRsP.next()) {
            chatLThrowUserID = chatLRsP.getString(1)
        }
        return chatLThrowUserID
    }

    //채팅넘버 찾은 뒤 리스트 업데이트하기 -- for Category3
    fun chatLGetListInChatNo(chatLOtherUser:String){
        chatLpstmt4.setString(1,userId)
        chatLpstmt4.setString(2,chatLOtherUser)
        chatLpstmt4.setString(3,chatLOtherUser)
        chatLpstmt4.setString(4,userId)
        var chatLRsP = chatLpstmt4.executeQuery()
        while(chatLRsP.next()) {
            chatLGetList(chatLRsP.getInt(1), chatLOtherUser)
        }
    }

    // 리스트에서 선택한 CHATNO로 SELLEROUT, BUYEROUT 찾아서 세팅 -- for Delete 1
    fun chatLGetOfOutList(getChatNoAtList:Int){
        var chatLGetSeller: String
        var chatLGetBuyer: String
        var chatLGetSellerOut: String
        var chatLGetBuyerOut: String
        chatLpstmt5.setInt(1,getChatNoAtList)
        var chatLRsP = chatLpstmt5.executeQuery()
        while(chatLRsP.next()) {
            chatLGetSeller = chatLRsP.getString(1)
            chatLGetBuyer = chatLRsP.getString(2)
            chatLGetSellerOut = chatLRsP.getString(3)
            chatLGetBuyerOut = chatLRsP.getString(4)
            if(chatLGetSeller.equals(userId)){ // 내가 판매자 일 때
                if(chatLGetSellerOut.equals("N",ignoreCase = true)){
                    chatLSetOfOutListAtSeller(getChatNoAtList)
                    chatLUserNickC.removeItem(chatLGetListforUserNick(chatLGetBuyer))
                }
            }else if(chatLGetBuyer.equals(userId)) {
                if(chatLGetBuyerOut.equals("N",ignoreCase = true)) {
                    chatLSetOfOutListAtBuyer(getChatNoAtList)
                    chatLUserNickC.removeItem(chatLGetListforUserNick(chatLGetSeller))
                }
            }
        }
    }

    // 유저가 SELLER일때 SELLEROUT 업데이트 -- for Delete 2.1
    fun chatLSetOfOutListAtSeller(getChatNoForDelete:Int) {
        chatLpstmt6.setInt(1,getChatNoForDelete)
        chatLpstmt6.executeUpdate()
    }

    // 유저가 BUYER일때 BUYEROUT 업데이트 -- for Delete 2.2
    fun chatLSetOfOutListAtBuyer(getChatNoForDelete:Int) {
        chatLpstmt7.setInt(1,getChatNoForDelete)
        chatLpstmt7.executeUpdate()
    }

    fun chatLcloseAll(){
        chatLpstmt7.close()
        chatLpstmt6.close()
        chatLpstmt5.close()
        chatLpstmt4.close()
        chatLpstmt3.close()
        chatLpstmt2.close()
        chatLpstmt1.close()
        chatLpstmt0.close()
    }

    override fun actionPerformed(e: ActionEvent) {
        var chatLobj = e.source
        when (chatLobj) {
            chatLLogoB -> {
                chatLcloseAll()
                dispose()
                Main(tower!!, userId,"All")
            }
            chatLUserNickC ->{
                charLcategorySetUI(chatLUserNickC.selectedItem.toString())
            }
            chatLDeleteB->{
                val chatLMyPostDeleteBAnwser = JOptionPane.showConfirmDialog(null, "정말로 삭제하시겠습니까?", "주의", JOptionPane.YES_NO_OPTION)
                if (chatLMyPostDeleteBAnwser == JOptionPane.YES_OPTION) {
                    var chatLListvalue1 = Integer.parseInt(chatLSelectedData1.toString())    //선택한 셀의 CHATNO
                    chatLGetOfOutList(chatLListvalue1)
                    chatLDataList.clear()
                    chatLMy(userId)
                }else {

                }
            }
        }
    }

    inner class mouseAdaptor : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            if (e.getClickCount() === 2) {
                var chatLListvalue1 = chatLSelectedData1
                var chatLListvalue2 = chatLSelectedData2
                //채팅방에 보내줄 값 <USERID(나), CHATNO, POSTNO>
                Chat(tower!!, userId,""+chatLListvalue2,""+chatLListvalue1)
            }
        }
    }
    inner class chatLLabelCellRenderer : ListCellRenderer<Vector<Any>>, JPanel() {

        private val chatLListCNick = JLabel()
        private val chatLListCTitle = JLabel()
        private val chatLListCRecentContent = JLabel()
        private val chatLListCUdate = JLabel()
        var chatLCellMainP = JPanel()

        init{
            chatLCellMainP = JPanel(GridLayout(0, 1))

            layout = null
            background = Color.WHITE
            border = BorderFactory.createMatteBorder(0,0,1,0,Color.lightGray.darker())

            chatLCellMainP.setBounds(70,0,260,65)

            chatLListCNick.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,16)
            chatLListCNick.setBounds(0,0,70,65)
            chatLListCNick.horizontalAlignment = JLabel.CENTER

            chatLListCTitle.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
            chatLCellMainP.add(chatLListCTitle)

            chatLListCRecentContent.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
            chatLCellMainP.add(chatLListCRecentContent)

            chatLListCUdate.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,12)
            chatLListCUdate.setBounds(330,0,170,65)
            chatLListCUdate.horizontalAlignment = JLabel.CENTER

            add(chatLListCNick)
            add(chatLCellMainP)
            add(chatLListCUdate)
        }

        override fun getListCellRendererComponent(
            list: JList<out Vector<Any>>?,
            value: Vector<Any>?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            var textVal = value.toString()
            textVal = textVal.substring(1, textVal.length-1)
            var textList=textVal.split(", "+"")

            chatLListCNick.text=textList[1]
            chatLListCTitle.text="${textList[2]}"
            chatLListCRecentContent.text="[${textList[5]}]:  ${textList[3]}"
            chatLListCUdate.text=textList[4]

            if (isSelected) {
                background = Color(147,255,255)
                chatLCellMainP.background = Color(147,255,255)
                chatLSelectedData1=textList[0]
                chatLSelectedData2=textList[6]
            } else {
                background = Color.WHITE
                chatLCellMainP.background = Color.WHITE
            }
            return this
        }
    }
}