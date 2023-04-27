import java.awt.*
import java.awt.Image.SCALE_SMOOTH
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.*
import java.lang.Math.abs
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.filechooser.FileSystemView


class PostInfo : JFrame, ActionListener {
    var userId: String
    var postNo: Int

    var poincp = contentPane
    var poinMainP = JPanel()
    var poinUpP = JPanel()
    var poinDownP = newPostInfoJpanel()

    //Top Side 구성
    var poinLogoRowIcon = ImageIcon(Tower().path+"imgs\\PapayaLogo.png")
    var poinLogoB = JButton()
    var poinLogoBChek = 0
    var poinMypoStatC = JComboBox<String>()
    var poinOtherStatL = JLabel()

    // Middle 구성
    //내글 이미지
    var poinImageChooser= JFileChooser()
    var poinImageFilter = FileNameExtensionFilter("PNG", "PNG" , "JPEG", "BMP", "GIF", "ICO")
    lateinit var poinImageFolderPath:String
    var poinImagePathT = JTextField()
    var poinImageBrowseRowIcon = ImageIcon(Tower().path+"imgs\\fileIcon.png")
    var poinImageBrowseB = JButton()
    var poinPictureL = JLabel()
    //남의글 이미지
    var poinOtherPictureL = JLabel()
    var poinNickL = JLabel()
    var poinSeedRowIcon = ImageIcon(Tower().path+"imgs\\씨앗.png")
    var poinSeedLogoL = JLabel()
    var poinSeedL = JLabel()
    var poinCategoryC = JComboBox<String>()
    var poinCategoryL = JLabel()
    var poinPostTitleT = JTextField()
    var poinPostMainA = JTextArea()

    //Bottom Side 구성
    var poinPriceRowIcon = ImageIcon(Tower().path+"imgs\\PoinPricetag2.png")
    var poinPriceT = JTextField(20)
    var poinPriceL = JLabel()
    var poinChatRowIcon = ImageIcon(Tower().path+"imgs\\PoinChat.png")
    var poinChatB = JButton()
    var poinChatL = JLabel()
    var poinChatBCheck = 0
    var poinFavoriteAddRowIcon = ImageIcon(Tower().path+"imgs\\PoinHeartAdd.png")
    var poinFavoriteDelRowIcon = ImageIcon(Tower().path+"imgs\\PoinHeartDel.png")
    var poinFavoriteB = JButton()
    var poinModifyRowIcon = ImageIcon(Tower().path+"imgs\\PoinModify.png")
    var poinModifyB = JButton()
    var poinDeleteRowIcon = ImageIcon(Tower().path+"imgs\\PoinDelete.png")
    var poinDeleteB = JButton()
    var poinReplantRowIcon = ImageIcon(Tower().path+"imgs\\PoinReplant.png")
    var poinReplantB = JButton()

    //판매완료시 생성되는 것들
    var poinMypoStatL = JLabel()
    var poinMypoCategoryL = JLabel()
    var poinImageBrowseL = JLabel()
    var poinModifyL = JLabel()
    var poinDeleteL = JLabel()
    var poinReplantL = JLabel()

    lateinit var con: Connection

    lateinit var poinpstmt0: PreparedStatement  // 상세화면 들어오자마자 일단 다 받어!
    var poinWhoPostSql = "select * from POST where POSTNO=?"
    lateinit var checkPostNo:Any
    lateinit var checkUserId:Any
    lateinit var checkPostTitle:Any
    lateinit var checkCategoryNo:Any
    lateinit var checkPostPrice:Any
    lateinit var checkPostMain:Any
    var checkPostImageName:Any? = null  // 이미지는 null 이 가능하므로 '?' 필요함
    var checkPostImagePath:String = ""
    lateinit var checkPostRedate:LocalDate
    lateinit var checkPostStat:Any

    lateinit var poinpstmt1: PreparedStatement  // 유저정보(닉네임, 평점) 찾기용
    var poinUserInfoSql = "select USERNICK, nvl(USERP,0) from POST natural join USERINFO where USERID=? and POSTNO=?"
    lateinit var checkUserNick:Any
    var checkUserPoint:Any? = null  // 평점은 null 이 가능하므로 '?' 필요함

    lateinit var poinpstmt2: PreparedStatement  // 카테고리 찾기용
    var poinCategorySql = "select * from CATEGORYPM"
    var checkCategory:String? = null
    var poinCategoryNOList = ArrayList<String>()
    var poinCategoryTypeList = ArrayList<String>()

    lateinit var poinpstmt3: PreparedStatement  // 글 수정용
    var poinMypostModifySql = "update POST set POSTTITLE=?, CATEGORYPMNO=?, POSTCOST=?, POSTCONTENT=?, POSTIMAGE=?, POSTST=? where POSTNO=?"

    lateinit var poinpstmt4: PreparedStatement  // 글 삭제용
    var poinMypostDeleteSql = "update POST set POSTDELETE=? where POSTNO=?"

    lateinit var poinpstmt5: PreparedStatement  // 다시심기용
    var poinMypostReplantSql = "update POST set POSTREDATE=SYSDATE where POSTNO=?"
    var poinNow = LocalDate.now()

    // 관심목록용 3가지
    lateinit var poinpstmt6: PreparedStatement  // 현재 내 관심목록에 있는 글인지 확인
    var poinFavoriteCheckSql = "select * from FAVORITE where USERID=?"
    var poinCheckFavoritePostNo: Int? = null
    lateinit var poinpstmt7: PreparedStatement  // 현재 내 관심목록에 없으면 추가
    var poinFavoriteInsertSql = "insert into FAVORITE values(FAVORITE_SEQ.nextval,?,?)"
    lateinit var poinpstmt8: PreparedStatement  // 현재 내 관심목록에 있으면 제거
    var poinFavoriteDeleteSql = "delete from FAVORITE where POSTNO=? and USERID=?"
    lateinit var poinpstmt9: PreparedStatement  // 현재 내 관심목록에 있으면 제거
    var poinDealInsertSql = "insert into DEAL values(DEAL_SEQ.nextval, ?, (select USERID from USERINFO where USERNICK=?), ?, SYSDATE, null, null, 'N')"
    lateinit var poinpstmt10: PreparedStatement  // 현재 내 관심목록에 있으면 제거
    var poinDealGetBuyerSql = "select USERNICK from USERINFO where USERID in (select c.CHATBUYERID from USERINFO u join CHAT c on u.USERID=c.CHATSELLERID where u.USERID=? and c.POSTNO=?) order by USERID"
    var poinBuyerC = JComboBox<String>()

    var tower : Tower? = null


    // SQL연결
    fun poinMyPostKdbc(){
        println("드라이버 로딩 성공")   // 검증!!!
        poinpstmt0 = con.prepareStatement(poinWhoPostSql)
        poinpstmt1 = con.prepareStatement(poinUserInfoSql)
        poinpstmt2 = con.prepareStatement(poinCategorySql)
        poinpstmt3 = con.prepareStatement(poinMypostModifySql)
        poinpstmt4 = con.prepareStatement(poinMypostDeleteSql)
        poinpstmt5 = con.prepareStatement(poinMypostReplantSql)
        poinpstmt6 = con.prepareStatement(poinFavoriteCheckSql)
        poinpstmt7 = con.prepareStatement(poinFavoriteInsertSql)
        poinpstmt8 = con.prepareStatement(poinFavoriteDeleteSql)
        poinpstmt9 = con.prepareStatement(poinDealInsertSql)
        poinpstmt10 = con.prepareStatement(poinDealGetBuyerSql)
    }

    // 게시글 누구껀지 확인 + 관련정보 다 받기 기능
    fun poinWhoPost(postNo: Int){
        poinpstmt0.setInt(1,postNo)
        var poinRsP = poinpstmt0.executeQuery()
        while(poinRsP.next()) {
            checkPostNo = poinRsP.getObject(1)
            checkUserId = poinRsP.getObject(2)
            checkPostTitle = poinRsP.getObject(3)
            checkCategoryNo = poinRsP.getObject(4)
            checkPostPrice = poinRsP.getObject(5)
            checkPostMain = poinRsP.getObject(6)
            checkPostImageName = poinRsP.getObject(7)
            checkPostStat = poinRsP.getObject(8)
            checkPostRedate = poinRsP.getDate(9).toLocalDate()
            for(i in 1 until 10) {  //검증
                println(poinRsP.getObject(i))
            }
        }
        if(!checkPostImageName.toString().equals("null")){
            checkPostImagePath = tower!!.path+"forUser\\${checkPostImageName.toString()}"
            println(checkPostImagePath)
        }else{
            checkPostImageName=""
        }
        var checkCategoryNoInt:Int = Integer.parseInt(checkCategoryNo.toString())
        poinCategory(checkCategoryNoInt)
        poinGetNick(checkUserId.toString())
    }

    // 유저정보(닉네임, 평점) 받기 기능
    fun poinGetNick(checkId: String){
        poinpstmt1.setString(1,checkId)
        poinpstmt1.setInt(2,postNo)
        var poinRsU = poinpstmt1.executeQuery()
        while(poinRsU.next()) {
            checkUserNick = poinRsU.getObject(1)
            checkUserPoint = poinRsU.getObject(2)
            println(checkUserNick);println(checkUserPoint)  // 검증
        }
    }

    // 카테고리 정보 받기 기능
    fun poinCategory(checkCategoryNoInt: Int){
        var poinRsC: ResultSet
        poinRsC = poinpstmt2.executeQuery()
        while(poinRsC.next()) {
            var poinCategoryNO = poinRsC.getObject(1).toString()
            poinCategoryNOList.add(poinCategoryNO)
            var poinCategoryType = poinRsC.getObject(2).toString()
            poinCategoryTypeList.add(poinCategoryType)
            poinCategoryC.addItem(poinCategoryType)
            if(poinCategoryNO.equals(checkCategoryNoInt.toString())){
                checkCategory = poinCategoryType
            }
        }
        println(checkCategory)  // 검증
    }

    // 이 글이 나한테 있으면 관심목록에 있는거면 버튼 바꿔주기
    fun poinFavoriteCheckB():Int {
        poinpstmt6.setString(1, userId)
        var poinRsP: ResultSet
        poinRsP = poinpstmt6.executeQuery()
        while (poinRsP.next()) {
            poinCheckFavoritePostNo = poinRsP.getInt(2)
            println(poinCheckFavoritePostNo)
            if(poinCheckFavoritePostNo == postNo){
                return 1
            }
        }
        return 2
    }

    //이미지 파일 열기 기능
    fun poinImageBrowse(): String {
        poinImageChooser = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
        poinImageChooser.isAcceptAllFileFilterUsed = true
        poinImageChooser.dialogTitle = "경로 탐색"
        poinImageChooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
        poinImageChooser.fileFilter = poinImageFilter
        val poinReturnVal: Int = poinImageChooser.showOpenDialog(null)
        if (poinReturnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
            poinImageFolderPath = poinImageChooser.selectedFile.toString()
        } else if (poinReturnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
            println("cancel")
            poinImageFolderPath = checkPostImagePath
        }
        return poinImageFolderPath
    }

    // 객체 생성시 UserID 랑 PostNO 받기
    constructor(tower:Tower, userId:String, postNo:Int){
        this.tower = tower
        this.con = tower.con!!
        poinMyPostKdbc()
        this.userId = userId
        this.postNo = postNo
        poinWhoPost(postNo)

        if (checkUserId.equals(userId)) {
            poinLogoBChek = 1
            if(checkPostStat.toString().trim().equals("판매완료")){
                poinInit1()
                poinSetDealEnd()
            }else{
                poinInit1()
            }
        } else {
            poinLogoBChek = 2
            if(checkPostStat.toString().trim().equals("판매완료")) {
                poinChatBCheck = 1
                poinInit2()
            } else{
                poinChatBCheck = 2
                poinInit2()
            }
        }
    }

    // 내글일 때 1번
    fun poinInit1(){
        poincp.add(poinMainP)
        poinMainP.add(poinUpP)
        poinMainP.add(poinDownP)
        poinMainP.layout = null
        poinMainP.background = Color.WHITE

        //위쪽 판넬
        poinUpP.layout = null
        poinUpP.setBounds(0,0,500,100)
        poinUpP.background= Color(240,255,255)

        poinUpP.border = BorderFactory.createMatteBorder(0,0,2,0, Color.lightGray)
        var poinLogoImage = poinLogoRowIcon.image.getScaledInstance(200,200, SCALE_SMOOTH)  // 로고이미지 작업
        var poinLogoIcon = ImageIcon(poinLogoImage)
        poinLogoB = JButton(poinLogoIcon)
        poinLogoB.setBounds(0,0,200,100)
        poinLogoB.isFocusPainted=false
        poinLogoB.isContentAreaFilled=false
        poinLogoB.isBorderPainted=false
        poinLogoB.toolTipText = "메인으로 돌아가기"
        poinLogoB.addActionListener(this)
        poinUpP.add(poinLogoB)

        //내 글일 때 상태창 콤보박스.. 나중에 메소드로 빼서 actionPerformed로 옮기기
        poinMypoStatC.setBounds(327,24,150,50)  // 상태창 콤보박스
        poinMypoStatC.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
        poinMypoStatC.background = Color.WHITE
        poinMypoStatC.addItem("판매중");poinMypoStatC.addItem("예약중");poinMypoStatC.addItem("판매완료")
        when(checkPostStat){   // 콤보박스에서 현재 상태인 것 띄우기.. 판매완료는 목록에 없을 것이므로 패스
            "판매중" -> poinMypoStatC.selectedIndex=0
            "예약중" -> poinMypoStatC.selectedIndex=1
        }
        var listRenderer = DefaultListCellRenderer()    // 콤보박스 Item 센터 맞추기
        listRenderer.horizontalAlignment = CENTER_ALIGNMENT.toInt()
        poinMypoStatC.renderer = listRenderer
        poinMypoStatC.addActionListener(this)
        poinUpP.add(poinMypoStatC)

        // 아래쪽 판넬 구성
        poinDownP.layout = null
        poinDownP.border = BorderFactory.createMatteBorder(2,0,0,0, Color.lightGray)
        poinDownP.setBounds(0,772,500,100)
        poinDownP.background=Color(240,255,255)

        // 내글 가격(텍필)
        poinPriceT.text = checkPostPrice.toString()
        poinPriceT.setBounds(5,30,155,40)
        poinPriceL.text = "                       원"
        poinPriceL.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,25)
        poinPriceL.foreground = Color.BLACK.darker()
        poinPriceL.setBounds(0,0,200,100)
        var poinPricetagImage = poinPriceRowIcon.image.getScaledInstance(200,100, SCALE_SMOOTH)
        var poinPricetagIcon = ImageIcon(poinPricetagImage)
        poinPriceL.icon = poinPricetagIcon
        poinPriceL.horizontalTextPosition = JLabel.CENTER
        poinPriceL.verticalTextPosition = JLabel.CENTER
        poinPriceT.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
        poinPriceT.foreground = Color.BLACK.brighter()
        poinPriceT.horizontalAlignment = JLabel.CENTER
        poinDownP.add(poinPriceL)
        poinDownP.add(poinPriceT)

        // 내글 수정
        poinModifyB.setBounds(200,0,100,100)
        var poinModifyImage = poinModifyRowIcon.image.getScaledInstance(70,70, SCALE_SMOOTH)
        var poinModifyIcon = ImageIcon(poinModifyImage)
        poinModifyB.icon = poinModifyIcon
        poinModifyB.isContentAreaFilled=false
        poinModifyB.isFocusPainted=false
        poinModifyB.toolTipText = "글 수정"
        poinModifyB.addActionListener(this)
        poinDownP.add(poinModifyB)

        // 내글 삭제
        poinDeleteB.setBounds(300,0,100,100)
        var poinDeleteImage = poinDeleteRowIcon.image.getScaledInstance(70,70, SCALE_SMOOTH)
        var poinDeleteIcon = ImageIcon(poinDeleteImage)
        poinDeleteB.icon = poinDeleteIcon
        poinDeleteB.isContentAreaFilled=false
        poinDeleteB.isFocusPainted=false
        poinDeleteB.toolTipText = "글 삭제"
        poinDeleteB.addActionListener(this)
        poinDownP.add(poinDeleteB)

        // 내글 다시심기
        poinReplantB.setBounds(400,0,100,100)
        var poinReplantImage = poinReplantRowIcon.image.getScaledInstance(70,70, SCALE_SMOOTH)
        var poinReplantIcon = ImageIcon(poinReplantImage)
        poinReplantB.icon = poinReplantIcon
        poinReplantB.isContentAreaFilled=false
        poinReplantB.isFocusPainted=false
        poinReplantB.toolTipText = "다시 심기"
        poinReplantB.addActionListener(this)
        poinDownP.add(poinReplantB)

        // 중앙판넬 구성
        // 브라우즈 경로
        poinImagePathT.text=checkPostImageName.toString()
        poinImagePathT.setBounds(15,355,250,30)
        poinImagePathT.isEditable=false
        poinImagePathT.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,13)
        poinMainP.add(poinImagePathT)

        // 브라우즈 버튼
        poinImageBrowseB.setBounds(270, 355, 30, 30)
        var poinImageBrowse = poinImageBrowseRowIcon.image.getScaledInstance(40,40, Image.SCALE_SMOOTH)
        var poinImageBrowseIcon: ImageIcon = ImageIcon(poinImageBrowse)
        poinImageBrowseB.icon = poinImageBrowseIcon
        poinImageBrowseB.background = Color.WHITE
        poinImageBrowseB.addActionListener(this)
        poinImageBrowseB.isBorderPainted = false
        poinImageBrowseB.isFocusPainted = false
        poinImageBrowseB.toolTipText = "이미지 파일 열기"
        poinMainP.add(poinImageBrowseB)

        // 내글 제품사진
        var poinProductRowIcon = ImageIcon(checkPostImagePath)
        var poinProductImage: Image = poinProductRowIcon.image.getScaledInstance(235,235, Image.SCALE_SMOOTH)
        var poinProductIcon = ImageIcon(poinProductImage)
        poinPictureL.setBounds(15,110,240,240)
        poinPictureL.icon = poinProductIcon
        poinMainP.add(poinPictureL)

        // 내글 글제목(텍필)
        poinPostTitleT.text = checkPostTitle.toString()
        poinPostTitleT.setBounds(15,395,465,30)
        poinPostTitleT.background = Color.LIGHT_GRAY
        poinPostTitleT.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        poinPostTitleT.foreground = Color.BLACK.brighter()
        poinPostTitleT.horizontalAlignment = JLabel.CENTER
        poinPostTitleT.isOpaque = true
        poinMainP.add(poinPostTitleT)

        // 내글 글내용(텍에)
        poinPostMainA.text = checkPostMain.toString()
        poinPostMainA.setBounds(15,430,465,330)
        poinPostMainA.background = Color.LIGHT_GRAY
        poinPostMainA.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        poinPostMainA.foreground = Color.BLACK.brighter()
        poinPostMainA.lineWrap = true
        poinPostMainA.isOpaque = true
        var poinPostMainSP = JScrollPane(poinPostMainA)
        poinPostMainSP.setBounds(15,430,465,330)
        poinPostMainSP.setViewportView(poinPostMainA)
        poinMainP.add(poinPostMainSP)

        // 닉네임 --- 공통
        poinNickL.text = checkUserNick.toString()
        poinNickL.setBounds(275,145,200,30)
        poinNickL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        poinNickL.foreground = Color.BLACK.brighter()
        poinNickL.horizontalAlignment = JLabel.CENTER
        poinMainP.add(poinNickL)

        // 씨앗 로고 --- 공통
        poinSeedLogoL.setBounds(275,215,100,100)
        poinSeedLogoL.horizontalAlignment = JLabel.CENTER
        var poinSeedImage = poinSeedRowIcon.image.getScaledInstance(100,100, SCALE_SMOOTH)  // 로고이미지 작업
        var poinSeedLogoIcon = ImageIcon(poinSeedImage)
        poinSeedLogoL.icon = poinSeedLogoIcon
        poinMainP.add(poinSeedLogoL)

        // 씨앗 평점 --- 공통
        poinSeedL.text = checkUserPoint.toString()
        poinSeedL.setBounds(385,240,80,40)
        poinSeedL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        poinSeedL.foreground = Color.BLACK.brighter()
        poinSeedL.horizontalAlignment = JLabel.CENTER
        poinMainP.add(poinSeedL)

       // 내글 카테고리 콤보박스
        poinCategoryC.setBounds(305,355,170,30)  // 상태창 콤보박스
        for (cType in poinCategoryTypeList) {   // 콤보박스에서 현재 카테고리인 것 띄우기
            if(checkCategory.equals(cType)){
                poinCategoryC.selectedIndex= poinCategoryTypeList.indexOf(checkCategory)
            }
        }
        poinCategoryC.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
        poinCategoryC.background = Color.WHITE
        poinCategoryC.renderer = listRenderer
        poinCategoryC.addActionListener(this)
        poinMainP.add(poinCategoryC)

        poinSetUI()
    }



    // 남의 글일 때 2번
    fun poinInit2() {
        poincp.add(poinMainP)
        poinMainP.add(poinUpP)
        poinMainP.add(poinDownP)
        poinMainP.layout = null
        poinMainP.background = Color.WHITE

        //위쪽 판넬
        poinUpP.layout = null  // 위쪽 Bar 넣을 것들
        poinUpP.background= Color(240,255,255)
        poinUpP.setBounds(0, 0, 500, 100)
        poinUpP.border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray)
        var poinLogoImage = poinLogoRowIcon.image.getScaledInstance(200, 200, SCALE_SMOOTH)  // 로고이미지 작업
        var poinLogoIcon = ImageIcon(poinLogoImage)
        poinLogoB = JButton(poinLogoIcon)
        poinLogoB.setBounds(0, 0, 200, 100)
        poinLogoB.isFocusPainted = false
        poinLogoB.isContentAreaFilled = false
        poinLogoB.isBorderPainted = false
        poinChatB.toolTipText = "메인으로 돌아가기"
        poinLogoB.addActionListener(this)
        poinUpP.add(poinLogoB)

        // 남의글 상태창(라벨)
        poinOtherStatL.text = checkPostStat.toString()
        poinOtherStatL.setBounds(327, 24, 150, 50)  // 상태창 라벨
        poinOtherStatL.background = Color(240,255,255)
        poinOtherStatL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        poinOtherStatL.foreground = Color.BLACK.brighter()
        poinOtherStatL.horizontalAlignment = JLabel.CENTER
        poinOtherStatL.isOpaque = true
        poinUpP.add(poinOtherStatL)

        // 아래쪽 판넬
        poinDownP.layout = null
        poinDownP.border = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.lightGray)
        poinDownP.setBounds(0, 772, 500, 100)
        poinDownP.background = Color(240, 255, 255)

        // 남의글 가격(라벨+노에딧텍필)
        poinPriceT.text = checkPostPrice.toString()
        poinPriceT.setBounds(5, 30, 155, 40)
        poinPriceL.text = "                       원"
        poinPriceL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        poinPriceL.foreground = Color.BLACK.darker()
        poinPriceL.setBounds(0, 0, 200, 100)
        var poinPricetagImage = poinPriceRowIcon.image.getScaledInstance(200, 100, SCALE_SMOOTH)
        var poinPricetagIcon = ImageIcon(poinPricetagImage)
        poinPriceL.icon = poinPricetagIcon
        poinPriceL.horizontalTextPosition = JLabel.CENTER
        poinPriceL.verticalTextPosition = JLabel.CENTER
        poinPriceT.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        poinPriceT.foreground = Color.BLACK.brighter()
        poinPriceT.horizontalAlignment = JLabel.CENTER
        poinPriceT.isEditable = false
        poinDownP.add(poinPriceL)
        poinDownP.add(poinPriceT)

        // 남의글 채팅 버튼
        when (poinChatBCheck) {
            1->{
                poinChatL.setBounds(200, 0, 150, 100)
                var poinChatImage = poinChatRowIcon.image.getScaledInstance(70, 70, Image.SCALE_SMOOTH)
                var poinChatIcon = ImageIcon(poinChatImage)
                poinChatL.icon = poinChatIcon
                poinChatL.horizontalAlignment = JLabel.CENTER
                poinDownP.add(poinChatL)
            }
            2-> {
                poinChatB.setBounds(200, 0, 150, 100)
                var poinChatImage = poinChatRowIcon.image.getScaledInstance(70, 70, Image.SCALE_SMOOTH)
                var poinChatIcon = ImageIcon(poinChatImage)
                poinChatB.icon = poinChatIcon
                poinChatB.isContentAreaFilled = false
                poinChatB.isFocusPainted = false
                poinChatB.toolTipText = "채팅 보내기"
                poinChatB.addActionListener(this)
                poinDownP.add(poinChatB)
            }
        }
        // 남의글 관심목록 버튼
        poinFavoriteB.setBounds(350, 0, 150, 100)
        when (poinFavoriteCheckB()) {
            1 -> {
                var poinFavoriteImage = poinFavoriteDelRowIcon.image.getScaledInstance(70, 70, Image.SCALE_SMOOTH)
                var poinFavoriteIcon = ImageIcon(poinFavoriteImage)
                poinFavoriteB.icon = poinFavoriteIcon
            }
            2 -> {
                var poinFavoriteImage = poinFavoriteAddRowIcon.image.getScaledInstance(70, 70, Image.SCALE_SMOOTH)
                var poinFavoriteIcon = ImageIcon(poinFavoriteImage)
                poinFavoriteB.icon = poinFavoriteIcon
            }
        }
        poinFavoriteB.isContentAreaFilled=false
        poinFavoriteB.isFocusPainted=false
        poinFavoriteB.toolTipText = "관심목록 추가/제거"
        poinFavoriteB.addActionListener(this)
        poinDownP.add(poinFavoriteB)


        // 중앙 판넬 구성
        // 남의글 제품사진
        poinOtherPictureL.setBounds(15,110,240,240)
        poinOtherPictureL.horizontalAlignment = JLabel.CENTER
        var poinOtherProductRowIcon:ImageIcon? = ImageIcon(checkPostImagePath)
        var poinProductImage = poinOtherProductRowIcon?.image?.getScaledInstance(235,235, SCALE_SMOOTH)  // 로고이미지 작업
        var poinProductIcon:ImageIcon? = ImageIcon(poinProductImage)
        poinOtherPictureL.icon = poinProductIcon
        poinMainP.add(poinOtherPictureL)

        // 닉네임 --- 공통
        poinNickL.text = checkUserNick.toString()
        poinNickL.setBounds(275,145,200,30)
        poinNickL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        poinNickL.foreground = Color.BLACK.brighter()
        poinNickL.horizontalAlignment = JLabel.CENTER
        poinMainP.add(poinNickL)

        // 씨앗 로고 --- 공통
        poinSeedLogoL.setBounds(275,215,100,100)
        poinSeedLogoL.horizontalAlignment = JLabel.CENTER
        var poinSeedImage = poinSeedRowIcon.image.getScaledInstance(100,100, SCALE_SMOOTH)  // 로고이미지 작업
        var poinSeedLogoIcon = ImageIcon(poinSeedImage)
        poinSeedLogoL.icon = poinSeedLogoIcon
        poinMainP.add(poinSeedLogoL)

        // 씨앗 평점 --- 공통
        poinSeedL.text = checkUserPoint.toString()
        poinSeedL.setBounds(385,240,80,40)
        poinSeedL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 25)
        poinSeedL.foreground = Color.BLACK.brighter()
        poinSeedL.horizontalAlignment = JLabel.CENTER
        poinMainP.add(poinSeedL)

        // 남의글 카테고리(라벨)
        poinCategoryL.text = checkCategory
        poinCategoryL.setBounds(275,355,200,30)
        poinCategoryL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        poinCategoryL.foreground = Color.BLACK.brighter()
        poinCategoryL.horizontalAlignment = JLabel.CENTER
        poinMainP.add(poinCategoryL)

        // 남의글 글제목(노에딧텍필)
        poinPostTitleT.text = checkPostTitle.toString()
        poinPostTitleT.setBounds(15,395,465,30)
        poinPostTitleT.background = Color.LIGHT_GRAY
        poinPostTitleT.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        poinPostTitleT.foreground = Color.BLACK.brighter()
        poinPostTitleT.horizontalAlignment = JLabel.CENTER
        poinPostTitleT.isEditable = false
        poinPostTitleT.isOpaque = true
        poinMainP.add(poinPostTitleT)

        // 남의글 글 내용(노에딧텍에)
        poinPostMainA.text = checkPostMain.toString()
        poinPostMainA.setBounds(15,430,465,330)
        poinPostMainA.background = Color.LIGHT_GRAY
        poinPostMainA.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 20)
        poinPostMainA.foreground = Color.BLACK.brighter()
        poinPostMainA.lineWrap = true
        poinPostMainA.isEditable = false
        var poinPostMainSP = JScrollPane(poinPostMainA)
        poinPostMainSP.setBounds(15,430,465,330)
        poinPostMainSP.setViewportView(poinPostMainA)
        poinMainP.add(poinPostMainSP)

        poinSetUI()
    }

    // 기본 세팅
    fun poinSetUI() {
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



    // 내글일 때 판매완료시 전체 edit=false 시키기
    fun poinSetDealEnd(){
        // 상태 콤보박스 -> 상태 라벨
        poinUpP.remove(poinMypoStatC)
        poinMypoStatL.text = checkPostStat.toString()
        poinMypoStatL.setBounds(327,24,150,50)  // 상태창 콤보박스
        poinMypoStatL.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
        poinMypoStatL.foreground = Color.BLACK.brighter()
        poinMypoStatL.background = Color.WHITE
        poinMypoStatL.horizontalAlignment = JLabel.CENTER
        poinUpP.add(poinMypoStatL)

        // 글 제목 -> 수정불가
        poinPostTitleT.isEditable = false

        //글 내용 -> 수정불가
        poinPostMainA.isEditable = false

        //이미지경로 내용 -> 수정불가
        poinImagePathT.isEditable = false

        //이미지 브라우즈 버튼 -> 이미지 브라우즈 라벨
        poinMainP.remove(poinImageBrowseB)
        poinImageBrowseL.setBounds(270, 360, 30, 30)
        var poinImageBrowse = poinImageBrowseRowIcon.image.getScaledInstance(40,40, Image.SCALE_SMOOTH)
        var poinImageBrowseIcon: ImageIcon = ImageIcon(poinImageBrowse)
        poinImageBrowseL.icon = poinImageBrowseIcon
        poinImageBrowseL.background = Color.WHITE
        poinMainP.add(poinModifyL)

        // 카테고리 콤보박스 -> 카테고리 라벨
        poinMainP.remove(poinCategoryC)
        poinMypoCategoryL.text = checkCategory
        poinMypoCategoryL.setBounds(275,345,200,30)  // 상태창 콤보박스
        poinMypoCategoryL.font = Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
        poinMypoCategoryL.foreground = Color.BLACK.brighter()
        poinMypoCategoryL.background = Color.WHITE
        poinMypoCategoryL.horizontalAlignment = JLabel.CENTER
        poinMainP.add(poinMypoCategoryL)

        //가격 내용 -> 수정불가
        poinPriceT.isEditable = false

        // 수정 버튼 -> 수정 라벨
        poinDownP.remove(poinModifyB)
        poinModifyL.setBounds(200,0,90,100)
        var poinModifyImage = poinModifyRowIcon.image.getScaledInstance(70,70, Image.SCALE_SMOOTH)
        var poinModifyIcon = ImageIcon(poinModifyImage)
        poinModifyL.icon = poinModifyIcon
        poinModifyL.horizontalAlignment = JLabel.CENTER
        poinDownP.add(poinModifyL)


        // 삭제 버튼 -> 삭제 라벨
        poinDownP.remove(poinDeleteB)
        poinDeleteL.setBounds(290,0,90,100)
        var poinDeleteImage = poinDeleteRowIcon.image.getScaledInstance(70,70, Image.SCALE_SMOOTH)
        var poinDeleteIcon = ImageIcon(poinDeleteImage)
        poinDeleteL.icon = poinDeleteIcon
        poinDeleteL.horizontalAlignment = JLabel.CENTER
        poinDownP.add(poinDeleteL)

        // 다시심기 버튼 -> 다시심기 라벨
        poinDownP.remove(poinReplantB)
        poinReplantL.setBounds(380,0,120,100)
        var poinReplantImage = poinReplantRowIcon.image.getScaledInstance(70,70, Image.SCALE_SMOOTH)
        var poinReplantIcon = ImageIcon(poinReplantImage)
        poinReplantL.icon = poinReplantIcon
        poinReplantL.horizontalAlignment = JLabel.CENTER
        poinDownP.add(poinReplantL)
    }

    // 내글일 때 글 수정 기능
    fun poinMyPostModify(statItem:String,categoryNo:String,titleMT:String,mainMT:String,priceMT:Int,poinImagePath:String){
        poinpstmt3.setString(1,titleMT)
        poinpstmt3.setString(2,categoryNo)
        poinpstmt3.setString(3,priceMT.toString())
        poinpstmt3.setString(4,mainMT)
        poinpstmt3.setString(5,poinImagePath)
        poinpstmt3.setString(6,statItem)
        poinpstmt3.setInt(7,postNo)
        poinpstmt3.executeUpdate()
    }

    // 내글일 때 글 삭제 기능
    fun poinMyPostDelete(){
        poinpstmt4.setString(1,"Y")
        poinpstmt4.setInt(2,postNo)
        poinpstmt4.executeUpdate()
    }

    // 내글일 때 다시심기 기능
    fun poinMyPostReplant() {
        poinpstmt5.setInt(1, postNo)
        poinpstmt5.executeUpdate()
    }

    // 남의 글일 때 관심목록 추가 기능
    fun poinFavoriteInsert(){
        poinpstmt7.setInt(1, postNo)
        poinpstmt7.setString(2, userId)
        poinpstmt7.executeUpdate()
    }

    // 남의 글일 때 관심목록 제거 기능
    fun poinFavoriteDelete(){
        poinpstmt8.setInt(1, postNo)
        poinpstmt8.setString(2, userId)
        poinpstmt8.executeUpdate()
    }

    // 내글일 때 다시심기 3일체크 기능
    fun poinMyPostCheckReplant(){
        val poinNowYear = poinNow.year
        val poinNowMonth = poinNow.monthValue
        val poinNowDay = poinNow.dayOfMonth
        val poinCheckYear = checkPostRedate.year
        val poinCheckMonth = checkPostRedate.monthValue
        val poinCheckDay = checkPostRedate.dayOfMonth
        var checkDateReplant = poinNowDay-poinCheckDay-3
        if(poinNowYear>poinCheckYear){
            poinMyPostReplant()
        }else if(poinNowYear==poinCheckYear){
            if(poinNowMonth>poinCheckMonth){
                poinMyPostReplant()
            }else if(poinNowMonth==poinCheckMonth){
                if((poinNowDay-poinCheckDay)>3){
                    poinMyPostReplant()
                }else{
                    JOptionPane.showMessageDialog(null,"지난 다시심기 후 3일이 지나지 않았습니다.${abs(checkDateReplant)}일 남음","경고",JOptionPane.PLAIN_MESSAGE)
                }
            }else{
                JOptionPane.showMessageDialog(null,"지난 다시심기 후 3일이 지나지 않았습니다.${abs(checkDateReplant)}일 남음","경고",JOptionPane.PLAIN_MESSAGE)
            }
        }else{
            JOptionPane.showMessageDialog(null,"지난 다시심기 후 3일이 지나지 않았습니다.${abs(checkDateReplant)}일 남음","경고",JOptionPane.PLAIN_MESSAGE)
        }
    }

    // 내글) 수정시 각종 체크 기능 -- 수정1
    fun textFieldCheckInActionPerformed(poinStatItem:String){
        var getCategoryNo=""
        var poinPostPriceMT:Int
        var poinCategoryItem = poinCategoryC.selectedItem
        for (cType in poinCategoryTypeList) {
            if (cType.equals(poinCategoryItem)) {
                getCategoryNo = poinCategoryNOList.get(poinCategoryTypeList.indexOf(cType))
            }
        }
        var poinPostTitleMT = poinPostTitleT.text
        if(poinPostTitleMT.length == 0 || poinPostTitleMT.length>18){
            JOptionPane.showMessageDialog(null, "제목이 없거나 너무 깁니다.", "경고", JOptionPane.PLAIN_MESSAGE)
        }else {
            var poinPostMainMT = poinPostMainA.text
            if(poinPostMainMT.length == 0 || poinPostMainMT.length>300) {
                JOptionPane.showMessageDialog(null, "글 내용이 없거나 너무 깁니다.", "경고", JOptionPane.PLAIN_MESSAGE)
            }else {
                try {
                    poinPostPriceMT = Integer.parseInt(poinPriceT.text)
                    if(poinPostPriceMT<0) {
                        JOptionPane.showMessageDialog(null, "가격이 0원 이하입니다.", "경고", JOptionPane.PLAIN_MESSAGE)
                    }else {
                        if(poinImagePathT.text.trim().isEmpty()){
                            var poinImagePath = poinImagePathT.text
                            poinMyPostModify(
                                poinStatItem,
                                getCategoryNo,
                                poinPostTitleMT,
                                poinPostMainMT,
                                poinPostPriceMT,
                                poinImagePath
                            )
                        }else {
                            var poinCheckLocalFile=File(poinImagePathT.text.trim())//새로 입력한 파일
                            var poinCheckServerFile=File(tower!!.path+"forUser\\"+poinImagePathT.text.trim())//기존에 있는 파일
                            if ((poinImagePathT.text.trim().equals("")) || (poinImagePathT.text.trim().equals("null"))) {
                                var poinImagePath = ""
                                poinMyPostModify(
                                    poinStatItem,
                                    getCategoryNo,
                                    poinPostTitleMT,
                                    poinPostMainMT,
                                    poinPostPriceMT,
                                    poinImagePath
                                )
                            }else if(poinCheckLocalFile.exists() || poinCheckServerFile.exists()){
                                var poinImagePath = poinImageFileCheck()
                                poinMyPostModify(
                                    poinStatItem,
                                    getCategoryNo,
                                    poinPostTitleMT,
                                    poinPostMainMT,
                                    poinPostPriceMT,
                                    poinImagePath
                                )
                            } else {
                                JOptionPane.showMessageDialog(null, "파일이 존재하지 않음", "에러", JOptionPane.PLAIN_MESSAGE)
                            }
                        }
                    }
                } catch (nfe: NumberFormatException) {
                    JOptionPane.showMessageDialog(null,"가격은 숫자로 입력해주세요.","경고",JOptionPane.PLAIN_MESSAGE)
                }
            }
        }
    }


    // 내글) 수정시 파일 체크 기능 -- 수정2
    fun poinImageFileCheck(): String{
        var poinImageFileName=checkPostImageName.toString()
        if(!poinImagePathT.text.trim().equals(checkPostImageName)) {
            var poinCheckFile=File(poinImagePathT.text.trim())
            val poinImageUpdateSDF = SimpleDateFormat("yyyyMMdd_HHmmss")
            val poinImageUpdateNow = Date()
            val poinImageUpdateNowTime = poinImageUpdateSDF.format(poinImageUpdateNow)

            println(poinImageUpdateNowTime)  //검증
            poinImageFileName = userId + "_" + poinImageUpdateNowTime + "_" + poinCheckFile.name
            println(poinImageFileName)  //검증

            var iStream: InputStream?
            val os: OutputStream
            val bs = ByteArray(8)
            var count = 0
            iStream = FileInputStream(poinCheckFile.canonicalPath)
            val poinImagePath = tower!!.path+"forUser\\$poinImageFileName"
            println(poinImagePath)
            os = FileOutputStream(poinImagePath) //실제파일

            while (iStream.read(bs).also { count = it } != -1) {
                os.write(bs, 0, count)
                os.flush()
            }
        }
        println(poinImageFileName)
        return poinImageFileName
    }


    // 판매완료 시 DEAL Insert 기능
    fun poinInsertDeal(poinBuyerValue:String){
        println("3333333333")
        poinpstmt9.setString(1, userId)
        poinpstmt9.setString(2, poinBuyerValue)
        poinpstmt9.setInt(3, postNo)
        poinpstmt9.executeUpdate()
    }


    fun poinGetBuyerNickForDeal(){
        poinpstmt10.setString(1,userId)
        poinpstmt10.setInt(2,postNo)
        var poinRsP = poinpstmt10.executeQuery()
        poinBuyerC.addItem("취소를 선택합니다")
        while(poinRsP.next()) {
            println(poinRsP.getString(1))
            poinBuyerC.addItem(poinRsP.getObject(1).toString())
        }
    }

    fun closeAll(){
        poinpstmt9.close()
        poinpstmt8.close()
        poinpstmt7.close()
        poinpstmt6.close()
        poinpstmt5.close()
        poinpstmt4.close()
        poinpstmt3.close()
        poinpstmt2.close()
        poinpstmt1.close()
        poinpstmt0.close()
    }

    override fun actionPerformed(e: ActionEvent) {
        var poinobj = e.source
        when (poinobj) {
            poinLogoB -> {
                when(poinLogoBChek){
                    1 -> {
                        val poinLogoBAnwser = JOptionPane.showConfirmDialog(null,"메인화면으로 돌아가시겠습니까?","주의",JOptionPane.YES_NO_OPTION)
                        if (poinLogoBAnwser == JOptionPane.YES_OPTION) {
                            dispose()
                            closeAll()
                            Main(tower!!, userId,"All")
                        }//N일때 아무일도 안생김
                    }
                    2 -> {
                        dispose()
                        closeAll()
                        Main(tower!!, userId,"All")
                    }//N일때 아무일도 안생김
                }
            }
            poinImageBrowseB -> {
                poinImagePathT.text = poinImageBrowse()
                var poinProductRowIcon = ImageIcon(poinImagePathT.text)
                var poinProductImage: Image = poinProductRowIcon.image.getScaledInstance(235,235, Image.SCALE_SMOOTH)
                var poinProductIcon = ImageIcon(poinProductImage)
                poinPictureL.icon = poinProductIcon
                poinPictureL.repaint()
            }
            poinModifyB -> {
                // 내용 수정 후 수정버튼 누르면 Y/N 선택옵션
                val poinModifyBAnwser = JOptionPane.showConfirmDialog(null,"정말로 수정하시겠습니까?","주의",JOptionPane.YES_NO_OPTION)
                if (poinModifyBAnwser == JOptionPane.YES_OPTION) {
                    var poinStatItem = poinMypoStatC.selectedItem.toString()
                    if (poinStatItem.equals("판매완료")) {
                        val poinMypoStatCAnwser = JOptionPane.showConfirmDialog(null,"[판매완료]로 변경하시면 더이상 수정이 불가능합니다.","주의",JOptionPane.YES_NO_OPTION)
                        if (poinMypoStatCAnwser == JOptionPane.YES_OPTION) {
                            poinGetBuyerNickForDeal()
                            if(poinBuyerC.itemCount == 1){
                                JOptionPane.showMessageDialog(null,"대화한 구매자가 없습니다")
                            }else {
                                var poinSellerListValue = JOptionPane.showMessageDialog(null, poinBuyerC, poinBuyerC.getItemAt(0),JOptionPane.QUESTION_MESSAGE)
                                var poinBuyerNick = poinBuyerC.selectedItem.toString()
                                if (poinSellerListValue === null) {
                                    JOptionPane.showMessageDialog(null, "판매완료를 하려면 반드시 선택해야합니다.")
                                }else if(poinBuyerNick.equals("취소를 선택합니다")) {
                                    JOptionPane.showMessageDialog(null, "판매완료 취소")
                                }else if(!poinBuyerNick.equals(null) || !poinBuyerNick.equals("")){
                                    println(poinBuyerNick)
                                    textFieldCheckInActionPerformed(poinStatItem)
                                    poinInsertDeal(poinBuyerNick) //나는 판매자
                                    dispose()
                                    var pi = PostInfo(tower!!, userId, postNo)
                                    pi.poinWhoPost(postNo)
                                    pi.poinSetDealEnd()
                                }
                            }
                        }
                        poinBuyerC.removeAllItems()
                    } else {
                        textFieldCheckInActionPerformed(poinStatItem)
                        dispose()
                        PostInfo(tower!!, userId, postNo)
                    }
                }else if (poinModifyBAnwser == JOptionPane.NO_OPTION){
                    dispose()
                    PostInfo(tower!!, userId, postNo)
                }
            }
            poinDeleteB -> {
                // 글 삭제버튼 누르면 Y/N 선택옵션
                val poinMyPostDeleteBAnwser = JOptionPane.showConfirmDialog(null, "정말로 삭제하시겠습니까?", "주의", JOptionPane.YES_NO_OPTION)
                if (poinMyPostDeleteBAnwser == JOptionPane.YES_OPTION) {
                    poinMyPostDelete()
                    dispose()
                    closeAll()
                    Main(tower!!, userId, "All")
                }else if (poinMyPostDeleteBAnwser == JOptionPane.NO_OPTION){
                    dispose()
                    PostInfo(tower!!, userId, postNo)
                }
            }
            poinReplantB -> {
                // 글 다시심기 누르면 Y/N 선택옵션
                val poinReplantBAnwser = JOptionPane.showConfirmDialog(null, "다시심기 하시겠습니까?(다시심기 후 3일간 변경 불가)", "주의", JOptionPane.YES_NO_OPTION)
                if (poinReplantBAnwser == JOptionPane.YES_OPTION) {
                    poinMyPostCheckReplant()
                    dispose()
                    PostInfo(tower!!, userId, postNo)
                }else if (poinReplantBAnwser == JOptionPane.NO_OPTION){
                    dispose()
                    PostInfo(tower!!, userId, postNo)
                }
            }
            poinChatB -> {
                // 글 채팅보내기 누르면 Y/N 선택옵션
                val poinChatBAnwser = JOptionPane.showConfirmDialog(null, "[${checkUserNick}]님에게 채팅을 보내시겠습니까?", "확인", JOptionPane.YES_NO_OPTION)
                if (poinChatBAnwser == JOptionPane.YES_OPTION) {    //Y일때 채팅창 클래스 불러오기
                    println("poinChatB실행")
                    Chat(tower!!, userId,""+postNo)  //Test 중~!!!!!!!!!!!!!!!!!!!!!
                }   //N일때 아무일도 안생김
            }
            poinFavoriteB -> {
                // 글 채팅보내기 누르면 Y/N 선택옵션
                val poinFavoriteBAnwser = JOptionPane.showConfirmDialog(null, "해당 게시물을 관심목록에 추가/제거하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION)
                if (poinFavoriteBAnwser == JOptionPane.YES_OPTION) {
                    when(poinFavoriteCheckB()){
                        1 -> {
                            poinFavoriteDelete()
                            var poinFavoriteImage =
                                poinFavoriteAddRowIcon.image.getScaledInstance(70, 70, Image.SCALE_SMOOTH)
                            var poinFavoriteIcon = ImageIcon(poinFavoriteImage)
                            poinFavoriteB.icon = poinFavoriteIcon
                        }
                        2 -> {
                            poinFavoriteInsert()
                            var poinFavoriteImage = poinFavoriteDelRowIcon.image.getScaledInstance(70, 70, Image.SCALE_SMOOTH)
                            var poinFavoriteIcon = ImageIcon(poinFavoriteImage)
                            poinFavoriteB.icon = poinFavoriteIcon
                        }
                    }
                }   //N일때 아무일도 안생김
            }
        }
    }
}

class newPostInfoJpanel: JPanel(){
    override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val g2d = g as Graphics2D
            var poinImage = Toolkit.getDefaultToolkit().getImage(Tower().path+"imgs\\papayatree2.png")
            g2d.drawImage(poinImage, 0, -30, this)
    }
}