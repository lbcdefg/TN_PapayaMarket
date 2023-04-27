import java.awt.Color
import java.awt.Font
import java.awt.Image
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import javax.swing.*
import java.awt.*

class Login : JFrame ,ActionListener, KeyListener{
    var con : Connection? = null
    var lgCheckLoginSql = "select USERID, USERPW from USERINFO where USERID=?"
    var lgCheckLoginPstmt : PreparedStatement? = null //id

    val cc1 = Color(240, 255, 255)

    var lgMainP = JPanel()
    var lgTopP = JPanel()
    var lgBotP = JPanel()

    var maLogoI = ImageIcon()
    val lgLogoL = JLabel(maLogoI)

    var lgIDL = JLabel("ID: ")
    var lgPWL = JLabel("PWD: ")

    var lgIDTf = JTextField()
    var lgPWTf = JPasswordField()

    var lgJoinB = JButton("회원가입")
    var lgLoginB = JButton("로그인")
    var logLogoB = JButton()

    var tower : Tower? = null



    constructor(tower:Tower) {
        this.tower = tower
        this.con = tower.con
        println(this.tower!!.path)
        println(tower.path)
        loginInit()
        loginSetUI()
    }

    fun loginInit() {

        var cp = contentPane

        lgMainP.setBounds(0,0,500,900)
        lgMainP.background = cc1

        lgTopP.setBounds(0,0,500,100)
        lgTopP.background =cc1
        lgTopP.layout = null
        lgMainP.add(lgTopP)

        lgBotP.setBounds(0,701,500,171)
        lgBotP.background =cc1
        lgBotP.layout = null
        lgMainP.add(lgBotP)

        lgMainP.layout = null
        cp.add(lgMainP)

        var logLogoCgB = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")
        var logLogoImg: Image = logLogoCgB.image
        var logLogoChageImg: Image = logLogoImg.getScaledInstance(400,400, Image.SCALE_SMOOTH)
        var logLogoChageIcon: ImageIcon = ImageIcon(logLogoChageImg)

        logLogoB.icon = logLogoChageIcon
        logLogoB.isContentAreaFilled=false
        logLogoB.isBorderPainted=false
        logLogoB.isFocusPainted=false
        logLogoB.addActionListener(this)
        logLogoB.setBounds(45,100,400,300)
        lgMainP.add(logLogoB)

        lgLogoL.setBounds(0,20,500,500)
        lgMainP.add(lgLogoL)

        lgIDL.setBounds(100,400,80,30)
        lgIDL.font= Font("나눔스퀘어 네오 Bold" , Font.BOLD,18)
        lgMainP.add(lgIDL)

        lgPWL.setBounds(70,460,80,30)
        lgPWL.font= Font("나눔스퀘어 네오 Bold" , Font.BOLD,18)
        lgMainP.add(lgPWL)

        lgJoinB.setBounds(130, 547,98,28)
        lgJoinB.addActionListener(this)
        lgJoinB.font= Font("나눔스퀘어 네오 Bold" , Font.BOLD,15)
        lgJoinB.background=Color.WHITE
        lgJoinB.isFocusPainted=false
        lgMainP.add(lgJoinB)

        lgLoginB.setBounds(278, 547,98,28)
        lgLoginB.addActionListener(this)
        lgLoginB.font= Font("나눔스퀘어 네오 Bold" , Font.BOLD,15)
        lgLoginB.background=Color.WHITE
        lgLoginB.isFocusPainted=false
        lgMainP.add(lgLoginB)

        lgIDTf.setBounds(180,400,160,30)
        lgIDTf.font=Font("나눔스퀘어 네오 Bold" , Font.BOLD,15)
        lgIDTf.addKeyListener(this)
        lgMainP.add(lgIDTf)

        lgPWTf.setBounds(180,460,160,30)
        lgPWTf.font=Font("나눔스퀘어 네오 Bold" , Font.BOLD,15)
        lgPWTf.addKeyListener(this)
        lgMainP.add(lgPWTf)

        var lgBotImg = JLabel()
        lgBotImg.icon=ImageIcon(tower!!.path+"imgs\\papayatree.png")
        lgBotImg.setBounds(0,0,500,171)
        lgBotP.add(lgBotImg)

    }

    fun loginSetUI() {
        title="TN Project Papaya Market"
        setSize(500, 900)
        var tk = Toolkit.getDefaultToolkit()
        var icon = tk.getImage(tower!!.path+"imgs\\파파야마켓.png")
        iconImage=icon
        isVisible=true
        setLocationRelativeTo(null)
        isResizable=false
        defaultCloseOperation=EXIT_ON_CLOSE
    }

    fun lgCloseAll(){
        lgCheckLoginPstmt?.close()
    }

    override fun actionPerformed(e:ActionEvent) { //회원가입
        var an = e.source
        if(an === lgJoinB) {
            lgCloseAll()
            dispose()
            Join(tower!!)
        }
        val myPass: String = java.lang.String.valueOf(lgPWTf.text)
        println(myPass)
        var obj = e.source
        if (obj === lgLoginB) {
            if (lgIDTf.text.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "아이디를 입력하세요", "TN Project Papaya Market", JOptionPane.INFORMATION_MESSAGE)
                lgIDTf.requestFocus()
            } else if (!lgIDTf.text.trim().isEmpty() && myPass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "비밀번호를 입력하세요", "TN Project Papaya Market", JOptionPane.INFORMATION_MESSAGE)
                lgPWTf.requestFocus()
            } else if (!lgPWTf.text.trim().isEmpty() && !myPass.isEmpty()) {
                loginCheck()
            }

        }
    }

    fun loginCheck(){
        lgCheckLoginPstmt = con!!.prepareStatement(lgCheckLoginSql)
        var userId = lgIDTf.text
        lgCheckLoginPstmt!!.setString(1, userId)
        var rs = lgCheckLoginPstmt!!.executeQuery()
        if(rs.next()){
            println(rs.getString(1))
            println(rs.getString(2))
            println()
            if(lgPWTf.text.equals(rs.getString(2))){

                JOptionPane.showMessageDialog(null, "환영합니다", "TN Project Papaya Market", JOptionPane.PLAIN_MESSAGE)
                println("메인화면으로 이동")
                lgCloseAll()
                dispose()
                Main(tower!!, userId, "All")
            }else{
                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "TN Project Papaya Market", JOptionPane.INFORMATION_MESSAGE)
            }
        }else{
            JOptionPane.showMessageDialog(null, "아이디가 존재하지 않습니다.", "TN Project Papaya Market", JOptionPane.INFORMATION_MESSAGE)
        }

    }
    override fun keyPressed(e: KeyEvent){ //작성창에서 Enter키로 채팅 전송
        lgCheckLoginPstmt = con!!.prepareStatement(lgCheckLoginSql)
        var userId = lgIDTf.text
        lgCheckLoginPstmt!!.setString(1, userId)
        var rs = lgCheckLoginPstmt!!.executeQuery()
        var keyCode:Int = e.keyCode
        when(keyCode){
            10 -> if(rs.next()){
                println(rs.getString(1))
                println(rs.getString(2))
                println()
                if(lgPWTf.text.equals(rs.getString(2))){
                    JOptionPane.showMessageDialog(null, "환영합니다", "TN Project Papaya Market", JOptionPane.PLAIN_MESSAGE)
                    println("메인화면으로 이동")
                    lgCloseAll()
                    dispose()
                    Main(tower!!, userId, "All")
                }else{
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "TN Project Papaya Market", JOptionPane.INFORMATION_MESSAGE)
                }
            }else{
                    JOptionPane.showMessageDialog(null, "아이디가 존재하지 않습니다.", "TN Project Papaya Market", JOptionPane.INFORMATION_MESSAGE)
            }
        }
    }
    override fun keyReleased(e: KeyEvent) {}
    override fun keyTyped(e: KeyEvent) {}
}

//로그인 이벤트 처리 logintf1:id logintf2:패스워드 loginb2: 로그인버튼



