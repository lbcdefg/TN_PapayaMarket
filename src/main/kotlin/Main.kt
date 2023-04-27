import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*


class Main: JFrame, ActionListener{

    var con: Connection?=null
    var maSeAddrPstmt: PreparedStatement?=null
    var maSeCPstmt: PreparedStatement?=null
    var maSeAlPstmt: PreparedStatement?=null
    var maSeSePstmt: PreparedStatement?=null
    var maSeFavstmt: PreparedStatement?=null

    var maMainP= JPanel()

    var maNewB= JButton()
    var maFavB= JButton()
    var maChB = JButton()
    var maInfoB= JButton()
    var maLogoB= JButton()

    var malogoCgB = ImageIcon()
    var maFavCgB = ImageIcon()
    var maInfoCgB = ImageIcon()
    var maChCgB = ImageIcon()
    var maLogoCgB = ImageIcon()

    var maaddrL = JLabel()
    var maTopImg = JLabel()
    var maBotImg = JLabel()

    var maSrchTf= JTextField ()

    var maCatCb= JComboBox<String>()
    var maCatList=Vector<String>()


    var maPostJl = JList<Vector<Any>>()
    var maPostDlm =DefaultListModel<Vector<Any>>()

    var userId = String()
    var address = String()

    var tower : Tower? = null



    //생성자 유저 아이디 받아서 로그인 유지
    constructor(tower: Tower, userId:String, listType:String){
        this.userId=userId
        this.tower = tower
        this.con = tower.con
        conn()
        //들어올때 타입이 관심목록인지 아님 전체인지 분류
        if(listType.equals("Fav"))
            selectFavList()
        else
            selectAllList("전체")
        init()
        setUI()
    }
    //디비 연결 하자마자 유저의 데이터와 카테고리 리스트 불러오기
    fun conn(){
        var selectAddrSql = "select USERADDR from USERINFO where USERID=?"
        maSeAddrPstmt = con!!.prepareStatement(selectAddrSql);
        maSeAddrPstmt!!.setString(1, userId)
        var addressRs = maSeAddrPstmt!!.executeQuery()
        addressRs.next()
        address = addressRs.getString(1)

        var selectCaSql = "select CATEGORYPMTYPE from CATEGORYPM"
        maSeCPstmt = con!!.prepareStatement(selectCaSql);
        var catList = maSeCPstmt!!.executeQuery()
        maCatList.add("전체")
        while(catList.next()){
            maCatList.add(catList.getString(1))
        }
    }
    fun init(){
        var cp :Container = contentPane
        maMainP= JPanel()
        maMainP.setBounds(0,0,500,900)
        maMainP.background=Color.WHITE
        cp.add(maMainP)
        maMainP.layout=null

        var maTopP = JPanel()
        maTopP.setBounds(0,0,500,100)
        maMainP.add(maTopP)
        maTopP.background=Color(240,255,255)
        maTopP.border=BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK)
        maTopP.layout=null

        var maBotP = JPanel()
        maBotP.setBounds(0,772,500,100)
        maBotP.background=Color(240,255,255)
        maBotP.border=BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK)
        maMainP.add(maBotP)
        maBotP.layout=null

        malogoCgB = ImageIcon(tower!!.path+"imgs\\Plus.png")
        var malogoImg: Image = malogoCgB.image
        var malogoChageImg:Image = malogoImg.getScaledInstance(70,70, Image.SCALE_SMOOTH)
        var malogoChageIcon = ImageIcon(malogoChageImg)

        maNewB = JButton(malogoChageIcon)
        maNewB.isContentAreaFilled=false
        maNewB.isFocusPainted=false
        maNewB.toolTipText = "새글 작성"
        maNewB.addActionListener(this)
        maNewB.setBounds(0,0,125,100)
        maBotP.add(maNewB)

        maFavCgB = ImageIcon(tower!!.path+"imgs\\Hearts.png")
        var maFavImg: Image = maFavCgB.image
        var maFavChageImg:Image = maFavImg.getScaledInstance(70,70, Image.SCALE_SMOOTH)
        var maFavChageIcon: ImageIcon = ImageIcon(maFavChageImg)

        maFavB = JButton(maFavChageIcon)
        maFavB.isContentAreaFilled=false
        maFavB.isFocusPainted=false
        maFavB.toolTipText = "관심목록 보기"
        maFavB.addActionListener(this)
        maFavB.setBounds(125,0,125,100)
        maBotP.add(maFavB)

        maChCgB = ImageIcon(tower!!.path+"imgs\\Chat.png")
        var maChImg: Image = maChCgB.image
        var maChChageImg:Image = maChImg.getScaledInstance(70,70, Image.SCALE_SMOOTH)
        var maChChageIcon = ImageIcon(maChChageImg)

        maChB = JButton(maChChageIcon)
        maChB.isContentAreaFilled=false
        maChB.isFocusPainted=false
        maChB.toolTipText = "채팅 리스트 보기"
        maChB.addActionListener(this)
        maChB.setBounds(250,0,125,100)
        maBotP.add(maChB)

        maInfoCgB = ImageIcon(tower!!.path+"imgs\\Info.png")
        var maInfoImg: Image = maInfoCgB.image
        var maInfoChageImg:Image = maInfoImg.getScaledInstance(70,70, Image.SCALE_SMOOTH)
        var maInfoChageIcon: ImageIcon = ImageIcon(maInfoChageImg)

        maInfoB = JButton(maInfoChageIcon)
        maInfoB.isContentAreaFilled=false
        maInfoB.isFocusPainted=false
        maInfoB.toolTipText = "내정보 보기"
        maInfoB.addActionListener(this)
        maInfoB.setBounds(375,0,125,100)
        maBotP.add(maInfoB)

        maLogoCgB = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")
        var maLogoImg: Image = maLogoCgB.image
        var maLogoChageImg:Image = maLogoImg.getScaledInstance(200,200, Image.SCALE_SMOOTH)
        var maLogoChageIcon = ImageIcon(maLogoChageImg)

        maLogoB.icon = maLogoChageIcon
        maLogoB.isContentAreaFilled=false
        maLogoB.isBorderPainted=false
        maLogoB.isFocusPainted=false
        maLogoB.toolTipText = "메인으로 돌아가기"
        maLogoB.addActionListener(this)
        maLogoB.setBounds(0,0,200,100)
        maTopP.add(maLogoB)

        maSrchTf = JTextField()
        maSrchTf.setBounds(0,100,300,40)
        maSrchTf.addKeyListener(keyListenerSearch())
        maSrchTf.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
        maMainP.add(maSrchTf)

        maCatCb = JComboBox(maCatList)
        maCatCb.setBounds(300,100,194,39)
        maCatCb.background=Color.white
        maCatCb.addActionListener(this)
        maCatCb.font=Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
        maMainP.add(maCatCb)

        maaddrL = JLabel(address)
        maaddrL.setBounds(300,40,200,30)
        maaddrL.font=Font("나눔스퀘어 네오 Bold",Font.BOLD,25)
        maTopP.add(maaddrL)

        var sp = JScrollPane(maPostJl)
        sp.setBounds(0,140,510,632)
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        maMainP.add(sp)

        maTopImg = JLabel()
        maTopImg.setBounds(0,0,500,125)
        maTopP.add(maTopImg)

        maBotImg = JLabel()
        maBotImg.icon=ImageIcon(tower!!.path+"imgs\\papayatree.png")
        maBotImg.setBounds(0,-20,500,125)
        maBotP.add(maBotImg)
    }
    fun maCloseAll(){
        maSeAddrPstmt?.close()
        maSeCPstmt?.close()
        maSeAlPstmt?.close()
        maSeFavstmt?.close()
        maSeSePstmt?.close()
    }

    override fun actionPerformed(e: ActionEvent) {
        var an = e.source
        //카테고리가 변하면 카테고리에 맞게 리스트 새로 불러오기
        if(an === maCatCb) {
            var select = maCatCb.getSelectedItem().toString()
            selectAllList(select)
        }
        //로고 버튼을 누르면 모든 리스트 불러오기
        if(an === maLogoB) {
            maCatCb.selectedIndex=0
            selectAllList("전체")
        }
        //관심목록 누르면 관심목록으로 리스트 바꿔주기
        if(an === maFavB) {
            maCatCb.selectedIndex=0
            selectFavList()
        }
        if(an === maNewB) {
            maCloseAll()
            dispose()
            PostIn(tower!!, userId)
            println("새글 클릭")
        }
        if(an === maChB) {
            maCloseAll()
            dispose()
            ChatList(tower!!, userId)
            println("채팅 클릭")
        }
        if(an === maInfoB) {
            maCloseAll()
            dispose()
            Myinfo(tower!!, userId)
            println("내정보 클릭")
        }
    }

    //검색창이 변하면 실시간으로 검색하면서 카테고리에 맞게 리스트 새로고침
    inner class keyListenerSearch: KeyAdapter() {
        override fun keyReleased(e:KeyEvent) {
            var category=maCatCb.getSelectedItem().toString()
            var str = maSrchTf.text
            if(str != null) str = str.trim();
            if(str.length !=0) {
                selectSearchList(str, category);
            } else {
                selectAllList(category);
            }
        }
    }


    //리스트에서 아이템을 두번 클릭하면 상세화면으로 이동
    inner class mouseAdaptor : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            if (e.getClickCount() === 2) {
                println(selectedValue)
                PostInfo(tower!!, userId, Integer.parseInt(selectedValue))
                println("상세화면 클릭")
                maCloseAll()
                dispose()
            }
        }
    }

    fun setUI() {
        title="TN Project Papaya Market"
        setSize(500, 900)
        var tk = Toolkit.getDefaultToolkit()
        var icon = tk.getImage(tower!!.path+"imgs\\파파야마켓.png")
        iconImage=icon
        isVisible=true
        setLocationRelativeTo(null)
        isResizable=false
        defaultCloseOperation=JFrame.EXIT_ON_CLOSE
    }

    //카테고리 또는 모든 리스트에 맞춰서 새로고침
    fun selectAllList(kind:String){
        maPostDlm.removeAllElements()
        var selectPoSql = String()

        if(kind.equals("전체")) {
            selectPoSql =
                "select POSTNO, POSTTITLE, POSTCOST, POSTST, POSTIMAGE, u.USERADDR from POST p join USERINFO u on p.USERID = u.USERID where POSTDELETE='N' and u.USERADDR=(select USERADDR from USERINFO where USERID=?) order by POSTREDATE desc"
        }else{
            selectPoSql =
                "select POSTNO, POSTTITLE, POSTCOST, POSTST, POSTIMAGE from POST p join USERINFO u on p.USERID = u.USERID where POSTDELETE='N' and u.USERADDR=(select USERADDR from USERINFO where USERID=?) and CATEGORYPMNO=(select CATEGORYPMNO from CATEGORYPM where CATEGORYPMTYPE=?)  order by POSTREDATE desc"
        }
        maSeAlPstmt = con!!.prepareStatement(selectPoSql);
        maSeAlPstmt!!.setString(1, userId)
        if(!kind.equals("전체")){
            maSeAlPstmt!!.setString(2, kind)
        }
        var postList = maSeAlPstmt!!.executeQuery()
        var rsmd = postList.metaData
        var cc = rsmd.columnCount
        while(postList.next()){
            var data = Vector<Any>()
            for(i in 1..cc){
                data.add(postList.getString(i))
            }
            maPostDlm.addElement(data)
        }
        maPostJl = JList<Vector<Any>>(maPostDlm)
        maPostJl.setCellRenderer(IconCellRenderer())
        maPostJl.addMouseListener(mouseAdaptor())
        maSeAlPstmt?.close()
    }

    //카테고리와 검색어에 맞춰서 리스트 새로고침
    fun selectSearchList(search:String ,kind:String){
        maPostDlm.removeAllElements()
        var selectPostSql = String()


        if(kind.equals("전체")) {
            selectPostSql =
                "select POSTNO, POSTTITLE, POSTCOST, POSTST, POSTIMAGE from POST p join USERINFO u on p.USERID = u.USERID where POSTDELETE='N' and u.USERADDR=(select USERADDR from USERINFO where USERID=?) and (POSTTITLE like ? or POSTCONTENT like ?) order by POSTREDATE desc"
        }else{
            selectPostSql =
                "select POSTNO, POSTTITLE, POSTCOST, POSTST, POSTIMAGE from POST p join USERINFO u on p.USERID = u.USERID where POSTDELETE='N' and u.USERADDR=(select USERADDR from USERINFO where USERID=?) and CATEGORYPMNO=(select CATEGORYPMNO from CATEGORYPM where CATEGORYPMTYPE= ?) and (POSTTITLE like ? or POSTCONTENT like ?) order by POSTREDATE desc"
        }

        maSeSePstmt = con!!.prepareStatement(selectPostSql);
        maSeSePstmt!!.setString(1, userId)
        if(kind.equals("전체")){
            maSeSePstmt!!.setString(2, "%$search%")
            maSeSePstmt!!.setString(3, "%$search%")
        }else{
            maSeSePstmt!!.setString(2, kind)
            maSeSePstmt!!.setString(3, "%$search%")
            maSeSePstmt!!.setString(4, "%$search%")
        }
        var postList = maSeSePstmt!!.executeQuery()
        var rsmd = postList.metaData
        var cc = rsmd.columnCount
        while(postList.next()){
            var data = Vector<Any>()
            for(i in 1..cc){
                data.add(postList.getString(i))
            }
            maPostDlm.addElement(data)
        }
        maPostJl = JList<Vector<Any>>(maPostDlm)
        maPostJl.setCellRenderer(IconCellRenderer())
        maPostJl.addMouseListener(mouseAdaptor())
        maSeSePstmt?.close()
    }

    //로그인된 유저의 관심목록 리스트를 불러온다
    fun selectFavList(){
        maPostDlm.removeAllElements()
        var selectFavSql =
            "select POSTNO, POSTTITLE, POSTCOST, POSTST, POSTIMAGE from POST where POSTNO in (select POSTNO from FAVORITE where USERID = ?) and POSTDELETE ='N'"
        maSeFavstmt = con!!.prepareStatement(selectFavSql);
        maSeFavstmt!!.setString(1, userId)

        var postList = maSeFavstmt!!.executeQuery()
        var rsmd = postList.metaData
        var cc = rsmd.columnCount
        var selectPostList: Vector<Vector<Any>> = Vector<Vector<Any>>()
        while(postList.next()){
            var data = Vector<Any>()
            for(i in 1..cc){
                data.add(postList.getString(i))
            }
            maPostDlm.addElement(data)
            selectPostList.add(data)
        }

        maPostJl = JList(maPostDlm)
        maPostJl.setCellRenderer(IconCellRenderer())
        maPostJl.addMouseListener(mouseAdaptor())
        maSeFavstmt?.close()
    }

    var selectedValue = String()
    //리스트 위에 판넬을 올리고 꾸며주는 ListCellRenderer
    inner class IconCellRenderer : ListCellRenderer<Vector<Any>>, JPanel() {


        private val lbIcon = JLabel()
        private val lbTitle = JLabel()
        private val lbCost = JLabel()
        private val lbState = JLabel()
        var panelText = JPanel()


        init{
            layout = BorderLayout(0,0)
            panelText = JPanel(GridLayout(0, 1))
            panelText.background= Color.WHITE
            lbTitle.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
            panelText.add(lbTitle)
            lbCost.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
            panelText.add(lbCost)
            add(lbIcon, BorderLayout.WEST)
            add(panelText, BorderLayout.CENTER)
            lbState.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
            add(lbState, BorderLayout.EAST)

        }


        //처음 로딩시랑 마우스 올릴때마다 불러오는거같음
        //안에서 라벨을 데이터로 치환해주며 파일을 불러와서 뿌려줌
        override fun getListCellRendererComponent(
            list: JList<out Vector<Any>>?,
            value: Vector<Any>?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            var text = value.toString()
            text = text.substring(1, text.length-1)
            var textList=text.split(", " )
            var filePath:String
            if(textList[4].equals("null") || textList[4] == null){
                filePath = tower!!.path+"imgs\\씨앗.png"
            }else{
                filePath = tower!!.path+"forUser\\"+textList[4]
            }

            var image = File(filePath)
            val img = ImageIcon(ImageIO.read(image))
            var chageIcon: ImageIcon = ImageIcon(img.image.getScaledInstance(130,130, Image.SCALE_SMOOTH))

            lbIcon.icon=chageIcon
            var title = textList[1]
            if(title.length>14){
                title = title.substring(0, 13)+".."
            }

            lbTitle.text=title
            lbCost.text="가격: "+textList[2]
            lbState.text=textList[3]+" "
            if (isSelected == true) {
                background = Color(147,255,255)
                panelText.background= Color(147,255,255)
                selectedValue=textList[0]
                foreground = Color.black
            } else {
                background = Color.white
                panelText.background= Color.white
                foreground = Color.black
            }
            return this
        }

    }
}