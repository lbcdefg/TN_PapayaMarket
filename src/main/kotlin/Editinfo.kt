import java.awt.*
import java.awt.event.*
import java.util.regex.Pattern
import javax.swing.*

class Editinfo : JFrame, ActionListener {
    var editinfoConfirmB = JButton()
    var editinfoNickeditB = JButton()
    var editinfoTeleditB = JButton()
    var editinfoJoinB = JButton()
    var editAddrInputB = JButton("입력")

    var editinfoMainP = JPanel()

    var editlogoCgB = ImageIcon()

    var editinfoIdL = JLabel()
    var editinfoUseridL = JLabel()
    var editinfoNickL = JLabel()
    var editinfoPwdL = JLabel()
    var editinfoPwdcheckL = JLabel()
    var editinfoTelL = JLabel()
    var editinfoAddrL = JLabel()

    var editinfoNickTf = JTextField()
    var editinfoPwdTf = JTextField()
    var editinfoPwdcheckTf = JTextField()
    var editinfoTelTf = JTextField()
    var editinfoAddrTf = JTextField()

    var userId = String()
    var userNick = String()
    var userTel = String()
    var userAddr = String()

    var tower : Tower? = null

    constructor(tower:Tower, userId: String, userNick: String, userTel: String, userAddr: String) {
        this.tower = tower
        this.userId = userId
        this.userNick = userNick
        this.userTel = userTel
        this.userAddr = userAddr
        initialize()
        setUI()
    }

    fun initialize() {
        var cp: Container = contentPane
        editinfoMainP = JPanel()
        editinfoMainP.setBounds(0, 0, 500, 900)
        editinfoMainP.background = Color(240, 255, 255)
        cp.add(editinfoMainP)
        editinfoMainP.layout = null

        var backgroundP = JPanel()
        backgroundP.setBounds(0, 700, 612, 171)
        backgroundP.background = Color(240, 255, 255)
        editinfoMainP.add(backgroundP)

        var backgroundL = JLabel()
        backgroundL.setBounds(0, -90, 612, 171)
        backgroundL.icon = ImageIcon(tower!!.path+"imgs\\papayatree.png")
        backgroundP.add(backgroundL)

        editlogoCgB = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")
        var editlogoImg: Image = editlogoCgB.image
        var editlogoChageImg: Image = editlogoImg.getScaledInstance(400, 400, Image.SCALE_SMOOTH)
        var editlogoChageIcon: ImageIcon = ImageIcon(editlogoChageImg)


        editinfoJoinB = JButton("")
        editinfoJoinB.setBounds(38, 70, 400, 200)
        editinfoJoinB.isContentAreaFilled = false
        editinfoJoinB.isBorderPainted = false
        editinfoJoinB.toolTipText = "내정보창으로 돌아가기"
        editinfoJoinB.icon = editlogoChageIcon
        editinfoJoinB.addActionListener(this)
        editinfoMainP.add(editinfoJoinB)

        editinfoIdL = JLabel("ID :")
        editinfoIdL.setBounds(106, 320, 70, 30)
        editinfoIdL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoIdL)

        editinfoUseridL = JLabel(userId)
        editinfoUseridL.setBounds(160, 320, 200, 30)
        editinfoUseridL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoUseridL)

        editinfoNickL = JLabel("닉네임 :")
        editinfoNickL.setBounds(78, 370, 700, 30)
        editinfoNickL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoNickL)

        editinfoNickTf = JTextField()
        editinfoNickTf.text = userNick
        editinfoNickTf.isEditable = false
        editinfoNickTf.setBounds(160, 370, 200, 30)
        editinfoNickTf.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoNickTf)

        editinfoNickeditB = JButton("수정")
        editinfoNickeditB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 12)
        editinfoNickeditB.setBounds(380, 370, 70, 30)
        editinfoNickeditB.background = Color.WHITE
        editinfoNickeditB.isFocusPainted = false
        editinfoNickeditB.addActionListener(this)
        editinfoMainP.add(editinfoNickeditB)

        editinfoTeleditB = JButton("수정")
        editinfoTeleditB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 12)
        editinfoTeleditB.background = Color.WHITE
        editinfoTeleditB.isFocusPainted = false
        editinfoTeleditB.setBounds(380, 520, 70, 30)
        editinfoTeleditB.addActionListener(this)
        editinfoMainP.add(editinfoTeleditB)

        editinfoPwdL = JLabel("PWD :")
        editinfoPwdL.setBounds(80, 420, 70, 30)
        editinfoPwdL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoPwdL)

        editinfoPwdTf = JTextField()
        editinfoPwdTf.setBounds(160, 420, 200, 30)
        editinfoPwdTf.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoPwdTf)

        editinfoPwdcheckL = JLabel("PWD확인 :")
        editinfoPwdcheckL.setBounds(50, 470, 90, 30)
        editinfoPwdcheckL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoPwdcheckL)

        editinfoPwdcheckTf = JTextField()
        editinfoPwdcheckTf.setBounds(160, 470, 200, 30)
        editinfoPwdcheckTf.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoPwdcheckTf)

        editinfoTelL = JLabel("Tel :")
        editinfoTelL.setBounds(94, 520, 70, 30)
        editinfoTelL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoTelL)

        editinfoTelTf = JTextField()
        userTel = userTel.replace("-", "")
        println(userTel)
        editinfoTelTf.text = userTel
        editinfoTelTf.setBounds(160, 520, 200, 30)
        editinfoTelTf.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoTelTf.isEditable = false
        editinfoMainP.add(editinfoTelTf)

        editinfoAddrL = JLabel("주소 :")
        editinfoAddrL.setBounds(90, 570, 70, 30)
        editinfoAddrL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoAddrL)

        editinfoAddrTf = JTextField()
        editinfoAddrTf.text = userAddr
        joinResult = userAddr
        editinfoAddrTf.setBounds(160, 570, 200, 30)
        editinfoAddrTf.isEditable=false
        editinfoAddrTf.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoMainP.add(editinfoAddrTf)

        editAddrInputB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 12)
        editAddrInputB.setBounds(380, 570, 70, 30)
        editAddrInputB.background = Color.WHITE
        editAddrInputB.isFocusPainted = false
        editAddrInputB.addActionListener(this)
        editinfoMainP.add(editAddrInputB)

        editinfoConfirmB = JButton("수정")
        editinfoConfirmB.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        editinfoConfirmB.background = Color.WHITE
        editinfoConfirmB.isFocusPainted = false
        editinfoConfirmB.setBounds(205, 640, 90, 30)
        editinfoConfirmB.addActionListener(this)
        editinfoMainP.add(editinfoConfirmB)
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
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

    var joinInit2F = JFrame()
    var joinInit2P = JPanel()
    var joinAddrSiL = JLabel("시: ")
    var joinAddrSiTf = JTextField()
    var joinAddrDongL = JLabel("동/면/읍:")
    var joinAddrDongTf = JTextField()
    var joinResult = editinfoAddrTf.text

    fun initializeForAddr() {
        joinInit2F.add(joinInit2P)
        joinInit2P.background = Color(240, 255, 255)
        joinInit2P.layout = null

        joinAddrSiL.setBounds(10, 20, 30, 30)
        joinAddrSiL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinInit2P.add(joinAddrSiL)

        joinAddrSiTf.setBounds(40, 20, 80, 30)
        joinAddrSiTf.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinAddrSiTf.addActionListener(this)
        joinInit2P.add(joinAddrSiTf)

        joinAddrDongL.setBounds(135, 20, 75, 30)
        joinAddrDongL.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinInit2P.add(joinAddrDongL)

        joinAddrDongTf.setBounds(205, 20, 85, 30)
        joinAddrDongTf.font = Font("나눔스퀘어 네오 Bold", Font.BOLD, 15)
        joinAddrDongTf.addActionListener(this)
        joinInit2P.add(joinAddrDongTf)

        setUIForAddr()
    }

    fun setUIForAddr() {       //서브 프레임 세팅
        joinInit2F.title = "TN Project Papaya Market"
        joinInit2F.setSize(300, 100)
        joinInit2F.isVisible = true
        joinInit2F.setLocationRelativeTo(null)
        joinInit2F.isResizable = false
        joinInit2F.defaultCloseOperation = DISPOSE_ON_CLOSE
    }

    fun DongCheck(address: String): Boolean {
        var checkPoint = false
        if (address.contains("동")) {
            checkPoint = true
        } else if (address.contains("면")) {
            checkPoint = true
        } else if (address.contains("읍")) {
            checkPoint = true
        }
        return checkPoint
    }

    override fun actionPerformed(e: ActionEvent) {
        var obj = e.source
        var newNick = editinfoNickTf.text
        var currentId = editinfoUseridL.text
        var editinfoTel = editinfoTelTf.text.replace("-","")
        var editinfoPwdCheck = editinfoPwdTf.text
        var editinfoPwdCheckCheck = editinfoPwdcheckTf.text
        
        if (obj === editinfoJoinB) {
            EditinfoKdbc(tower!!).closeAll()
            dispose()
            Myinfo(tower!!, userId)
        }
        if (obj === editinfoNickeditB) {
            if (obj.text.equals("수정")) {
                obj.text = "중복"
                editinfoNickTf.isEditable = true
            } else if (obj.text.equals("중복")) {

                if (EditinfoKdbc(tower!!).selectNick(newNick, currentId)) {
                    JOptionPane.showMessageDialog(
                        null, "이미 사용중인 닉네임입니다", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newNick.equals("\n") || newNick.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null, "아무것도 입력하지 않았어요", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newNick.length > 6) {
                    JOptionPane.showMessageDialog(
                        null, "닉네임은 최대 6글자입니다", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else {
                    JOptionPane.showMessageDialog(null, "사용가능한 닉네임이에요")
                    obj.text = "수정"
                    editinfoNickTf.isEditable = false
                }
            }
        }
        if (obj === editinfoTeleditB) {
            if (obj.text.equals("수정")) {
                obj.text = "중복"
                editinfoTelTf.isEditable = true
            } else if (obj.text.equals("중복")) {
                try {
                    var newEditTelI = Integer.parseInt(editinfoTel)
                    println(newEditTelI)
                    if (editinfoTel.isEmpty()) {
                        JOptionPane.showMessageDialog(
                            null, "아무것도 입력하지 않았어요", "경고",
                            JOptionPane.ERROR_MESSAGE
                        )
                    } else if (editinfoTel.length !== 11) {
                        JOptionPane.showMessageDialog(
                            null, "전화번호는 11글자입니다", "경고",
                            JOptionPane.ERROR_MESSAGE
                        )
                    } else if (editinfoTel !== null) {
                        if (editinfoTel.contains("-")) {
                            JOptionPane.showMessageDialog(
                                null, "하이픈을 제외하고 입력해주세요", "경고",
                                JOptionPane.ERROR_MESSAGE
                            )
                        } else if (Pattern.matches("^[0-9]*$", editinfoTel) && editinfoTel.length == 11) {
                            var editinfoTelNo = editinfoTel.substring(0, 3) + "-" + editinfoTel.substring(
                                3,
                                7
                            ) + "-" + editinfoTel.substring(7)
                            if (EditinfoKdbc(tower!!).selectTel(editinfoTelNo, userId)) {
                                JOptionPane.showMessageDialog(
                                    null, "사용중인 전화번호입니다", "경고",
                                    JOptionPane.ERROR_MESSAGE
                                )
                            } else {
                                println(editinfoTel)
                                JOptionPane.showMessageDialog(null, "전화번호 사용가능")
                            }
                        }
                    }
                }catch(nfe: NumberFormatException){
                    JOptionPane.showMessageDialog(
                        null, "전화번호는 숫자로 입력해주세요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }
        }
        if (obj === editAddrInputB) {
            initializeForAddr()
        }
        if (obj === editinfoConfirmB) {
            try {
                var newEditTelI = Integer.parseInt(editinfoTel)
                println(newEditTelI)
                if (EditinfoKdbc(tower!!).selectNick(newNick, currentId)) {
                    JOptionPane.showMessageDialog(
                        null, "이미 사용중인 닉네임입니다", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else  if (newNick.equals("\n") || newNick.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null, "닉네임을 입력해주세요", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (newNick.length > 6) {
                    JOptionPane.showMessageDialog(
                        null, "닉네임은 최대 6글자입니다", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (!Pattern.matches("^[a-zA-Z0-9]*$", editinfoPwdCheck) || !Pattern.matches(
                        "^[a-zA-Z0-9]*$",
                        editinfoPwdCheckCheck
                    )
                ) {
                    JOptionPane.showMessageDialog(
                        null, "영어와 숫자만 가능해요", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (editinfoPwdCheck.isEmpty() || editinfoPwdCheckCheck.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null, "비밀번호를 입력해주세요", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (editinfoPwdCheck.length > 12 || editinfoPwdCheck.length < 4 || editinfoPwdCheckCheck.length > 12 || editinfoPwdCheckCheck.length < 4) {
                    JOptionPane.showMessageDialog(
                        null, "4~12자로 만들어주세요", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (!editinfoPwdCheck.equals(editinfoPwdCheckCheck)) {
                    JOptionPane.showMessageDialog(
                        null, "비밀번호가 일치하지 않아요", "앗! 이럴수가!",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (editinfoTel.length !== 11) {
                    JOptionPane.showMessageDialog(
                        null, "전화번호는 11글자입니다", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (joinResult.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null, "__시 __동을 입력해주세요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                } else if (editinfoTel !== null) {
                    if (editinfoTel.contains("-")) {
                        JOptionPane.showMessageDialog(
                            null, "하이픈을 제외하고 입력해주세요", "경고",
                            JOptionPane.ERROR_MESSAGE
                        )
                    } else if (Pattern.matches("^[0-9]*$", editinfoTel) && editinfoTel.length == 11) {
                        var editinfoTelNo = editinfoTel.substring(0, 3) + "-" + editinfoTel.substring(3, 7) + "-" + editinfoTel.substring(7)
                        if (EditinfoKdbc(tower!!).selectTel(editinfoTelNo, userId)) {
                            JOptionPane.showMessageDialog(
                                null, "사용중인 전화번호입니다", "경고",
                                JOptionPane.ERROR_MESSAGE
                            )
                        }else {
                            EditinfoKdbc(tower!!).updateEditInfo(newNick, editinfoPwdCheck, joinResult, editinfoTelNo, userId)
                            println(editinfoTel)
                            JOptionPane.showMessageDialog(null, "회원정보 수정완료")
                            EditinfoKdbc(tower!!).closeAll()
                            dispose()
                            Myinfo(tower!!, userId)
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        null, "주소를 제대로 입력해주세요", "경고",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }catch(nfe: NumberFormatException) {
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
            } else if (!joinGetSi.isEmpty() && !joinGetDong.isEmpty() && !joinGetDong.equals("\n") && !joinGetSi.equals("\n")){
                var checkJoinGetSi = joinGetSi.substring(2)
                println(checkJoinGetSi)
                if (checkJoinGetSi.contains("시")) {
                    println(joinGetSi)
                    if (DongCheck(joinGetDong)) {
                        println(joinGetDong)
                        joinResult = "${joinGetSi} ${joinGetDong}"
                        println(joinResult)
                        editinfoAddrTf.text = joinResult
                        joinInit2F.dispose()
                    } else {
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