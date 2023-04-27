import java.awt.*
import java.awt.event.*
import java.util.regex.Pattern
import javax.swing.*

class Join : JFrame, ActionListener {
    var joinIdcheckB = JButton()
    var joinNickcheckB = JButton()
    var joinAddrcheckB = JButton()
    var joinConfirmB = JButton()
    var joinTelcheckB = JButton()

    var joinMainP = JPanel()
    var joinMain2P = JPanel()

    var joinlogoCgB = ImageIcon()

    var joinJoinB = JButton()
    var joinIdL = JLabel()
    var joinNickL = JLabel()
    var joinPwdL = JLabel()
    var joinPwdcheckL = JLabel()
    var joinTelL = JLabel()
    var joinAddrL = JLabel()

    var joinIdTf = JTextField()
    var joinNickTf = JTextField()
    var joinPwdTf = JTextField()
    var joinPwdcheckTf = JTextField()
    var joinTelTf = JTextField()
    var joinAddrTf = JTextField()
    var joinAddrInputB = JButton("입력")

    var tower : Tower? = null

    constructor(tower: Tower) {
        this.tower = tower
        initialize()
        setUI()
    }
    fun initialize(){
        var cp :Container = contentPane
        joinMainP = JPanel()                //메인패널
        joinMainP.setBounds(0,0,500,125)
        joinMainP.background=Color(240,255,255)
        cp.add(joinMainP)
        joinMainP.layout=null

        var backgroundP = JPanel()          //아래 배경용 패널
        backgroundP.setBounds(0,700,612,171)
        backgroundP.background=Color(240,255,255)
        joinMainP.add(backgroundP)

        var backgroundL = JLabel()          //밑에 배경
        backgroundL.setBounds(0,-90,612,171)
        backgroundL.icon=ImageIcon(tower!!.path+"imgs\\papayatree.png")
        backgroundP.add(backgroundL)

        joinlogoCgB = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")      //로고사진 자연스럽게 축소
        var joinlogoImg: Image = joinlogoCgB.image
        var joinlogoChageImg:Image = joinlogoImg.getScaledInstance(400,400, Image.SCALE_SMOOTH)
        var joinlogoChageIcon: ImageIcon = ImageIcon(joinlogoChageImg)

        joinJoinB = JButton("")                 //로고를 로그인창으로 돌아가는 버튼으로 생성
        joinJoinB.setBounds(38, 70, 400, 200)
        joinJoinB.icon=joinlogoChageIcon
        joinJoinB.isContentAreaFilled=false
        joinJoinB.isBorderPainted=false
        joinJoinB.addActionListener(this)
        joinMainP.add(joinJoinB)

        joinIdL = JLabel("ID :")
        joinIdL.setBounds(106,320, 70, 30)
        joinIdL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinIdL)

        joinIdTf = JTextField()
        joinIdTf.setBounds(160, 320, 200, 30)
        joinIdTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinIdTf)

        joinIdcheckB= JButton("중복")
        joinIdcheckB.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 12)
        joinIdcheckB.setBounds(380,320,70,30)
        joinIdcheckB.background=Color.WHITE
        joinIdcheckB.isFocusPainted=false
        joinIdcheckB.addActionListener(this)
        joinMainP.add(joinIdcheckB)

        joinNickL = JLabel("닉네임 :")
        joinNickL.setBounds(78, 370, 700, 30)
        joinNickL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinNickL)

        joinNickTf = JTextField()
        joinNickTf.setBounds(160, 370, 200, 30)
        joinNickTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinNickTf)

        joinNickcheckB= JButton("중복")
        joinNickcheckB.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 12)
        joinNickcheckB.setBounds(380, 370, 70, 30)
        joinNickcheckB.background=Color.WHITE
        joinNickcheckB.isFocusPainted=false
        joinNickcheckB.addActionListener(this)
        joinMainP.add(joinNickcheckB)

        joinPwdL = JLabel("PWD :")
        joinPwdL.setBounds(80, 420, 70, 30)
        joinPwdL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinPwdL)

        joinPwdTf = JTextField()
        joinPwdTf.setBounds(160, 420, 200, 30)
        joinPwdTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinPwdTf)

        joinPwdcheckL = JLabel("PWD확인 :")
        joinPwdcheckL.setBounds(50, 470, 90, 30)
        joinPwdcheckL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinPwdcheckL)

        joinPwdcheckTf = JTextField()
        joinPwdcheckTf.setBounds(160, 470, 200, 30)
        joinPwdcheckTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinPwdcheckTf)

        joinTelL = JLabel("Tel :")
        joinTelL.setBounds(94, 520, 70, 30)
        joinTelL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinTelL)

        joinTelTf = JTextField()
        joinTelTf.setBounds(160, 520, 200, 30)
        joinTelTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinTelTf)

        joinTelcheckB= JButton("중복")
        joinTelcheckB.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 12)
        joinTelcheckB.setBounds(380, 520, 70, 30)
        joinTelcheckB.background=Color.WHITE
        joinTelcheckB.isFocusPainted=false
        joinTelcheckB.addActionListener(this)
        joinMainP.add(joinTelcheckB)

        joinAddrL = JLabel("주소 :")
        joinAddrL.setBounds(90, 570, 70, 30)
        joinAddrL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinMainP.add(joinAddrL)

        joinAddrTf = JTextField("__시 __동(읍/면)")
        joinAddrTf.setBounds(160, 570, 200, 30)
        joinAddrTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinAddrTf.horizontalAlignment = JTextField.CENTER
        joinAddrTf.isEditable = false
        joinMainP.add(joinAddrTf)


        joinAddrInputB.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 12)
        joinAddrInputB.setBounds(380, 570, 70, 30)
        joinAddrInputB.background=Color.WHITE
        joinAddrInputB.isFocusPainted=false
        joinAddrInputB.addActionListener(this)
        joinMainP.add(joinAddrInputB)

        joinConfirmB= JButton("확인")
        joinConfirmB.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinConfirmB.setBounds(205, 640, 90, 30)
        joinConfirmB.background=Color.WHITE
        joinConfirmB.isFocusPainted=false
        joinConfirmB.addActionListener(this)
        joinMainP.add(joinConfirmB)
    }
    fun setUI() {       //메인 프레임 세팅
        title = "TN Project Papaya Market"
        setSize(500,900)
        var tk = Toolkit.getDefaultToolkit()
        var icon = tk.getImage(tower!!.path+"imgs\\파파야마켓.png")
        iconImage=icon
        isVisible = true
        setLocationRelativeTo(null)
        isResizable = false
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
    }


    // 시, 동 입력창
    var joinInit2F = JFrame()
    var joinInit2P = JPanel()
    var joinAddrSiL = JLabel("시: ")
    var joinAddrSiTf = JTextField()
    var joinAddrDongL = JLabel("동/면/읍:")
    var joinAddrDongTf = JTextField()
    var joinResult = ""

    fun initializeForAddr(){
        joinInit2F.add(joinInit2P)
        joinInit2P.background=Color(240,255,255)
        joinInit2P.layout=null

        joinAddrSiL.setBounds(10, 20, 30, 30)
        joinAddrSiL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinInit2P.add(joinAddrSiL)

        joinAddrSiTf.setBounds(40, 20, 80, 30)
        joinAddrSiTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinAddrSiTf.addActionListener(this)
        joinInit2P.add(joinAddrSiTf)

        joinAddrDongL.setBounds(135, 20, 75, 30)
        joinAddrDongL.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinInit2P.add(joinAddrDongL)

        joinAddrDongTf.setBounds(205, 20, 85, 30)
        joinAddrDongTf.font=Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinAddrDongTf.addActionListener(this)
        joinInit2P.add(joinAddrDongTf)

        setUIForAddr()
    }

    fun setUIForAddr() {       //서브 프레임 세팅
        joinInit2F.title = "TN Project Papaya Market"
        joinInit2F.setSize(300,100)
        joinInit2F.isVisible = true
        joinInit2F.setLocationRelativeTo(null)
        joinInit2F.isResizable = false
        joinInit2F.defaultCloseOperation = DISPOSE_ON_CLOSE
    }

    fun DongCheck(address:String):Boolean{
        var checkPoint = false
        if(address.contains("동")){
            checkPoint = true
        }else if(address.contains("면")){
            checkPoint = true
        }else if(address.contains("읍")){
            checkPoint = true
        }
        return checkPoint
    }

    // ---------------------여기까지---------------------

    var newTel =String()

    override fun actionPerformed(e: ActionEvent) {  //액션
        var obj = e.source
        var newId = joinIdTf.text
        newId=newId.trim()
        var newNick = joinNickTf.text
        newTel = joinTelTf.text
        newNick= newNick.trim()
        newTel=newTel.trim()

        if (obj === joinJoinB){
            JoinKdbc(tower!!).closeAll()
            dispose()
            Login(tower!!)
        }
        if(obj === joinIdcheckB) {
            if (newId.isEmpty()) {
                JOptionPane.showMessageDialog(
                    null, "아무것도 입력하지 않았어요", "경고",
                    JOptionPane.ERROR_MESSAGE)
            } else if (JoinKdbc(tower!!).selectId(newId)) {
                JOptionPane.showMessageDialog(
                    null, "사용중인 ID", "경고",
                    JOptionPane.ERROR_MESSAGE)
            } else if (!Pattern.matches("^[a-zA-Z0-9]*$", newId)) {
                JOptionPane.showMessageDialog(
                    null, "영어와 숫자만 가능해요", "경고",
                    JOptionPane.ERROR_MESSAGE)
            }else if(newId.length >30){
                JOptionPane.showMessageDialog(
                    null, "ID는 최대 30글자입니다", "경고",
                    JOptionPane.ERROR_MESSAGE)
            }else{
               JOptionPane.showMessageDialog(null,"ID 사용가능")
            }
        }
        if(obj === joinNickcheckB){
            if (newNick.isEmpty()) {
                JOptionPane.showMessageDialog(
                    null, "아무것도 입력하지 않았어요", "경고",
                    JOptionPane.ERROR_MESSAGE)
            } else if (JoinKdbc(tower!!).selectNick(newNick)) {
                JOptionPane.showMessageDialog(
                    null, "사용중인 닉네임입니다", "경고",
                    JOptionPane.ERROR_MESSAGE)
            }else if(newNick.length >6){
                JOptionPane.showMessageDialog(
                    null, "닉네임은 최대 6글자입니다", "경고",
                    JOptionPane.ERROR_MESSAGE)
            }else{
                JOptionPane.showMessageDialog(null,"닉네임 사용가능")
            }
        }
        if(obj === joinAddrInputB){
            initializeForAddr()
        }
        if(obj === joinTelcheckB){
            try {
                var newTelI = Integer.parseInt(newTel)
                println(newTelI)
                if (newTel.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null, "아무것도 입력하지 않았어요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newTel.length !== 11) {
                    JOptionPane.showMessageDialog(
                        null, "전화번호는 11글자입니다", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newTel !== null) {
                    if (newTel.contains("-")) {
                        JOptionPane.showMessageDialog(
                            null, "하이픈을 제외하고 입력해주세요", "경고",
                            JOptionPane.ERROR_MESSAGE
                        )
                    } else if (Pattern.matches("^[0-9]*$", newTel) && newTel.length == 11) {
                        var newTelNo = newTel.substring(0, 3) + "-" + newTel.substring(3, 7) + "-" + newTel.substring(7)
                        if (JoinKdbc(tower!!).selectTel(newTelNo)) {
                            JOptionPane.showMessageDialog(
                                null, "사용중인 전화번호입니다", "경고",
                                JOptionPane.ERROR_MESSAGE
                            )
                        } else {
                            JOptionPane.showMessageDialog(null, "전화번호 사용가능")
                        }
                    }
                }
            }catch(nfe:NumberFormatException){
                JOptionPane.showMessageDialog(
                    null, "전화번호는 숫자로 입력해주세요", "경고",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
        if(obj === joinConfirmB) {
            var newPsw = joinPwdTf.text
            newPsw = newPsw.trim()
            var newPswCheck = joinPwdcheckTf.text
            newPswCheck = newPswCheck.trim()

            try {
                var newTelI = Integer.parseInt(newTel)
                println(newTelI)

                if (JoinKdbc(tower!!).selectId(newId)) {
                    JOptionPane.showMessageDialog(
                        null, "사용중인 ID에요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (!Pattern.matches("^[a-zA-Z0-9]*$", newPsw) || !Pattern.matches(
                        "^[a-zA-Z0-9]*$",
                        newPswCheck
                    )
                ) {
                    JOptionPane.showMessageDialog(
                        null, "영어와 숫자만 가능해요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (!newPsw.equals(newPswCheck)) {
                    JOptionPane.showMessageDialog(
                        null, "비밀번호가 일치하지 않아요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newPsw.length > 12 || newPsw.length < 4 || newPswCheck.length > 12 || newPswCheck.length < 4) {
                    JOptionPane.showMessageDialog(
                        null, "비밀번호는 4~12자 이내로 만들어주세요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (JoinKdbc(tower!!).selectNick(newNick)) {
                    JOptionPane.showMessageDialog(
                        null, "사용중인 닉네임입니다", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newNick.length > 6) {
                    JOptionPane.showMessageDialog(
                        null, "닉네임은 최대 6글자입니다", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newTel.length !== 11) {
                    JOptionPane.showMessageDialog(
                        null, "전화번호는 11글자입니다", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (joinResult.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null, "__시 __동을 입력해주세요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newTel !== null) {
                    if (newTel.contains("-")) {
                        JOptionPane.showMessageDialog(
                            null, "하이픈을 제외하고 입력해주세요", "경고",
                            JOptionPane.ERROR_MESSAGE
                        )
                    } else if (Pattern.matches("^[0-9]*$", newTel) && newTel.length == 11) {
                        var newTelNo = newTel.substring(0, 3) + "-" + newTel.substring(3, 7) + "-" + newTel.substring(7)
                        if(JoinKdbc(tower!!).selectTel(newTelNo)) {
                            JOptionPane.showMessageDialog(
                                null, "사용중인 전화번호입니다", "경고",
                                JOptionPane.ERROR_MESSAGE
                            )
                        } else {
                            JoinKdbc(tower!!).insertJoin(newId, newNick, newPsw, joinResult, newTelNo)
                            JOptionPane.showMessageDialog(null, "회원가입 완료")
                            JoinKdbc(tower!!).closeAll()
                            dispose()
                            Login(tower!!)
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        null, "주소입력방법(ex.'__시')", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }catch(nfe:NumberFormatException){
                JOptionPane.showMessageDialog(
                    null, "전화번호는 숫자로 입력해주세요", "경고",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
        if ((obj === joinAddrSiTf) || (obj === joinAddrDongTf)) {
            val joinGetSi = joinAddrSiTf.getText().trim() + "\n"
            val joinGetDong = joinAddrDongTf.getText().trim() + "\n"
            if (joinGetSi.isEmpty()) {
                JOptionPane.showMessageDialog(null, "'__시'를 입력해주세요", "경고", JOptionPane.ERROR_MESSAGE)
            } else if (joinGetDong.isEmpty()) {
                JOptionPane.showMessageDialog(null, "'__동(읍/면)'을 입력해주세요", "경고", JOptionPane.ERROR_MESSAGE)
            } else if(!joinGetSi.isEmpty() && !joinGetDong.isEmpty() && !joinGetDong.equals("\n") && !joinGetSi.equals("\n")){
                var checkJoinGetSi = joinGetSi.substring(2)
                println(checkJoinGetSi)
                if (checkJoinGetSi.contains("시")) {
                    println(joinGetSi)
                    if (DongCheck(joinGetDong)) {
                        println(joinGetDong)
                        joinResult = "${joinGetSi} ${joinGetDong}"
                        println(joinResult)
                        joinAddrTf.text = joinResult
                        joinInit2F.dispose()
                    }else{
                        JOptionPane.showMessageDialog(null, "'__동(읍/면)'를 입력해주세요", "경고", JOptionPane.ERROR_MESSAGE)
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "'__시'를 입력해주세요", "경고", JOptionPane.ERROR_MESSAGE)
                }
            } else {
                JOptionPane.showMessageDialog(null, "__시 __동을 정확히 입력해주세요", "경고", JOptionPane.ERROR_MESSAGE)
            }
        }
    }
}
