import java.awt.*
import java.awt.Container
import java.awt.Font
import java.awt.Image
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.filechooser.FileSystemView


class PostIn : JFrame, ActionListener {

	var con: Connection?=null

	var piPSeCPstmt: PreparedStatement?=null
	var piPInPstmt: PreparedStatement?=null

	var userId = String()

	var piMainP = JPanel()
	var piTopP= JPanel()
	var piBotP= JPanel()
	var piBotImg = JLabel()

	var piPII = ImageIcon()
	var piPIB= JButton()

	var piLogoB = JButton()
	var piPCancelI = ImageIcon()
	var piPCancelB= JButton()

	var piPTitleL= JLabel()
	var piPTitleTf= JTextField ()

	var piPCatCb= JComboBox<String>()
	var piPCatList=Vector<String>()

	var piPCostL= JLabel()
	var piPCostTf= JTextField ()

	var piPConSp=JScrollPane()
	var piPConTa=JTextArea()

	var piPFileL= JLabel()
	var piPFileTf= JTextField ()
	var piPFileI= ImageIcon ()
	var piPFileB= JButton ()
	var chooser= JFileChooser()
	var filter = FileNameExtensionFilter("PNG", "PNG", "JPEG", "ICO", "BMP")
	var fileName=""
	var folderPath=""
	var piPPhotoI = ImageIcon()
	val piPPhotoL = JLabel("")

	var tower : Tower? = null


	constructor(tower:Tower, userId:String) {
		this.tower = tower
		this.con = tower.con
		this.userId=userId

		conn()
		init()
		setUI()
	}

	fun conn(){

		var selectCaSql = "select CATEGORYPMTYPE from CATEGORYPM"
		println("1111111111111111111111")
		piPSeCPstmt = con!!.prepareStatement(selectCaSql);
		var catList = piPSeCPstmt!!.executeQuery()
		while(catList.next()){
			println(catList.getString(1))
			piPCatList.add(catList.getString(1))
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
		defaultCloseOperation=EXIT_ON_CLOSE

	}

	fun init() {
		var cp :Container = contentPane
		piMainP= JPanel()
		piMainP.setBounds(0,0,500,900)
		piMainP.background=Color.WHITE
		cp.add(piMainP)
		piMainP.layout=null

		piTopP.setBounds(0,0,500,100)
		piMainP.add(piTopP)
		piTopP.background=Color(240,255,255)
		piTopP.border=BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK)
		piTopP.layout=null

		piBotP.setBounds(0,772,500,100)
		piBotP.background=Color(240,255,255)
		piBotP.border=BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK)
		piMainP.add(piBotP)
		piBotP.layout=null

		var piLogoCgB = ImageIcon(tower!!.path+"imgs\\PapayaLogo.png")
		var maLogoImg: Image = piLogoCgB.image
		var maLogoChageImg:Image = maLogoImg.getScaledInstance(200,200, Image.SCALE_SMOOTH)
		var maLogoChageIcon = ImageIcon(maLogoChageImg)

		piLogoB = JButton()
		piLogoB.icon = maLogoChageIcon
		piLogoB.isContentAreaFilled=false
		piLogoB.isBorderPainted=false
		piLogoB.isFocusPainted=false
		piLogoB.addActionListener(this)
		piLogoB.setBounds(0,0,195,100)
		piTopP.add(piLogoB)

		piPII = ImageIcon(tower!!.path+"imgs\\registration.png")
		var piPIImg: Image = piPII.image
		var piPIChageImg:Image = piPIImg.getScaledInstance(70,70, Image.SCALE_SMOOTH)
		var piPIChageIcon = ImageIcon(piPIChageImg)

		piPIB = JButton()
		piPIB.isContentAreaFilled=false
		piPIB.isFocusPainted=false
		piPIB.addActionListener(this)
		piPIB.icon=piPIChageIcon
		piPIB.setBounds(0,0,125,100)
		piBotP.add(piPIB)

		piPCancelI = ImageIcon(tower!!.path+"imgs\\cancel.png")
		var piPCancelImg: Image = piPCancelI.image
		var piPCancelChageImg:Image = piPCancelImg.getScaledInstance(80,80, Image.SCALE_SMOOTH)
		var piPCancelChageIcon = ImageIcon(piPCancelChageImg)


		piPCancelB = JButton()
		piPCancelB.isContentAreaFilled=false
		piPCancelB.isFocusPainted=false
		piPCancelB.addActionListener(this)
		piPCancelB.icon=piPCancelChageIcon
		piPCancelB.setBounds(375,0,125,100)
		piBotP.add(piPCancelB)

		piPTitleL = JLabel("제목 : ")
		piPTitleL.setBounds(20,120,60,40)
		piPTitleL.font=Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
		piMainP.add(piPTitleL)

		piPTitleTf = JTextField()
		piPTitleTf.setBounds(80,120,400,40)
		piPTitleTf.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
		piMainP.add(piPTitleTf)

		piPCatCb = JComboBox(piPCatList)
		piPCatCb.setBounds(20,180,194,39)
		piPCatCb.background=Color.white
		piPCatCb.font=Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
		piMainP.add(piPCatCb)

		piPCostL = JLabel("가격 : ")
		piPCostL.setBounds(240,180,60,40)
		piPCostL.font=Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
		piMainP.add(piPCostL)

		piPCostTf = JTextField()
		piPCostTf.setBounds(300,180,180,40)
		piPCostTf.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
		piMainP.add(piPCostTf)

		piPConTa = JTextArea()
		piPConTa.font=Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
		piPConTa.isEditable=true
		piPConTa.isEnabled=true

		piPConSp = JScrollPane(piPConTa)
		piPConSp.setBounds(20,240,460,300)
		piMainP.add(piPConSp)

		piPFileL = JLabel("사진 : ")
		piPFileL.setBounds(20,560,60,40)
		piPFileL.font=Font("나눔스퀘어 네오 Bold",Font.BOLD,15)
		piMainP.add(piPFileL)

		piPFileTf = JTextField()
		piPFileTf.setBounds(80,560,350,40)
		piPFileTf.isEditable=false
		piPFileTf.font= Font("나눔스퀘어 네오 Bold",Font.BOLD,20)
		piMainP.add(piPFileTf)

		piPFileB = JButton()
		piPFileB.foreground = Color.WHITE
		piPFileI = ImageIcon(tower!!.path+"imgs\\fileIcon.png")
		var piPFileImg: Image = piPFileI.image
		var piPFileChageImg:Image = piPFileImg.getScaledInstance(40,40, Image.SCALE_SMOOTH)
		var piPFileChageIcon = ImageIcon(piPFileChageImg)
		piPFileB.icon = piPFileChageIcon
		piPFileB.background = Color.WHITE
		piPFileB.font = Font("나눔스퀘어 네오 Bold", Font.PLAIN, 11)
		piPFileB.setBounds(440, 560, 40, 40)
		piPFileB.addActionListener(this)
		piPFileB.isBorderPainted = false
		piPFileB.isFocusPainted = false
		piMainP.add(piPFileB)

		piPPhotoI = ImageIcon(tower!!.path+"imgs\\씨앗.png")
		var piPPhotoImg: Image = piPPhotoI.image
		var piPPhotoChageImg:Image = piPPhotoImg.getScaledInstance(100,100, Image.SCALE_SMOOTH)
		var piPPhotoChageIcon = ImageIcon(piPPhotoChageImg)

		piPPhotoL.icon = piPPhotoChageIcon
		piPPhotoL.setBounds(20, 620, 100, 100)
		piMainP.add(piPPhotoL)

		piBotImg = JLabel()
		piBotImg.icon=ImageIcon(tower!!.path+"imgs\\papayatree.png")
		piBotImg.setBounds(0,-20,500,125)
		piBotP.add(piBotImg)

	}

	fun browse(): String {
		chooser = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
		chooser.isAcceptAllFileFilterUsed = true
		chooser.dialogTitle = "경로 탐색"
		chooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
		chooser.fileFilter = filter
		val returnVal: Int = chooser.showOpenDialog(null)
		if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
			folderPath = chooser.selectedFile.toString()
		} else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
			println("cancel")
			folderPath = ""
		}
		return folderPath
	}
	fun piCloseAll(){
		piPSeCPstmt?.close()
		piPInPstmt?.close()
	}


	override fun actionPerformed(e: ActionEvent?) {
		val obj = e!!.source
		if (obj === piLogoB){
			val piModifyLogoAnwser = JOptionPane.showConfirmDialog(null,"메인 화면으로 이동하시겠습니까?","주의",JOptionPane.YES_NO_OPTION)
			if (piModifyLogoAnwser===JOptionPane.YES_OPTION){
				piCloseAll()
				dispose()
				Main(tower!!, userId, "All")
			}else{

			}

		}
		if (obj === piPFileB) {
			piPFileTf.text = browse()

			piPPhotoI = ImageIcon(piPFileTf.text)
			var piPPhotoImg: Image = piPPhotoI.image
			var piPPhotoChageImg:Image = piPPhotoImg.getScaledInstance(100,100, Image.SCALE_SMOOTH)
			var piPPhotoChageIcon = ImageIcon(piPPhotoChageImg)

			piPPhotoL.icon=piPPhotoChageIcon
			piPPhotoL.repaint()
		}
		if (obj === piPIB) {
			//--------------------------------데이터 체크
			//JOptionPane.showMessageDialog(null, "데이터 안맞음", "에러", JOptionPane.ERROR_MESSAGE)
			//--------------------------데이터 체크
			println("등록버튼 클릭")
			var title = piPTitleTf.text
			var category = piPCatCb.selectedItem.toString()
			var cost:Int
			var content = piPConTa.text
			var filePath = piPFileTf.text


			if(title.isEmpty() || title.length>18){
				JOptionPane.showMessageDialog(null, "제목이 없거나 너무 깁니다.", "경고", JOptionPane.PLAIN_MESSAGE)
			}else {
				if(content.isEmpty() || content.length>300) {
					JOptionPane.showMessageDialog(null, "글 내용이 없거나 너무 깁니다.", "경고", JOptionPane.PLAIN_MESSAGE)
				}else {
					try {
						cost = Integer.parseInt(piPCostTf.text)
						if(cost<0) {
							JOptionPane.showMessageDialog(null, "가격이 0원 이하입니다.", "경고", JOptionPane.PLAIN_MESSAGE)
						}else {
							var file=File(filePath)
							if(!filePath.equals("")&&!file.exists()){
								JOptionPane.showMessageDialog(null, "파일이 존재하지 않음", "에러", JOptionPane.ERROR_MESSAGE)
							}else if(!filePath.equals("")&&file.exists()) {

								val sdf1 = SimpleDateFormat("yyyyMMdd_HHmmss")
								val now = Date()
								val nowTime1 = sdf1.format(now)
								fileName = userId + "_" + nowTime1 + "_" + file.name

								var iStream: InputStream?
								val os: OutputStream
								val bs = ByteArray(8)
								var count = 0
								iStream = FileInputStream(file.canonicalPath)
								val path = tower!!.path+"forUser\\$fileName"
								println(path)
								os = FileOutputStream(path) //실제파일

								while (iStream.read(bs).also { count = it } != -1) {
									os.write(bs, 0, count)
									os.flush()
								}
							}


							var insertSql = "insert into POST values(POST_SEQ.nextval, ?, ?,(select CATEGORYPMNO from CATEGORYPM where CATEGORYPMTYPE=?),?,?,?,'판매중',SYSDATE,SYSDATE,'N')"
							piPInPstmt = con!!.prepareStatement(insertSql)
							piPInPstmt!!.setString(1, userId)
							piPInPstmt!!.setString(2, title)
							piPInPstmt!!.setString(3, category)
							piPInPstmt!!.setInt(4, cost)
							piPInPstmt!!.setString(5, content)
							piPInPstmt!!.setString(6, fileName)

							var i = piPInPstmt!!.executeUpdate()

							println(i)

							piPInPstmt!!.close()
							piCloseAll()
							dispose()
							Main(tower!!, userId,"All")
						}
					} catch (nfe: NumberFormatException) {
						JOptionPane.showMessageDialog(null,"가격은 숫자로 입력해주세요.","경고",JOptionPane.PLAIN_MESSAGE)
					}
				}
			}
		}
		if (obj === piPCancelB) {
			piCloseAll()
			dispose()
			Main(tower!!, userId, "All")
			println("취소버튼 클릭")
		}
	}
}