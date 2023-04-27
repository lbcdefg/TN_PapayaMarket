import java.sql.*

class JoinKdbc{
    var con : Connection? = null
    var jiIsrtPstmt : PreparedStatement? = null
    var jiUidPstmt : PreparedStatement? = null
    var jiUnickPstmt : PreparedStatement? = null
    var jiUtelPstmt: PreparedStatement?= null
    var jiIsrtsql = "insert into USERINFO values(?,?,?,REPLACE(REPLACE(REGEXP_REPLACE(?, '[0-9]'), '특별'), '광역'),?,null)"
    var jiUidsql = "select USERID from USERINFO where USERID= ?"
    var jiUnicksql = "select USERNICK from USERINFO where USERNICK=? "
    var jiUtelsql = "select USERTEL from USERINFO where USERTEL=?"

    var tower : Tower? = null
    constructor(tower: Tower){

        this.tower = tower
        this.con = tower.con
        try {
            jiIsrtPstmt = con!!.prepareStatement(jiIsrtsql)
            jiUidPstmt = con!!.prepareStatement(jiUidsql)
            jiUnickPstmt = con!!.prepareStatement(jiUnicksql)
            jiUtelPstmt = con!!.prepareStatement(jiUtelsql)
        }catch(cnfe: ClassNotFoundException){
            println("문제가뭐야?: " + cnfe)
        }catch(se: SQLException){
            println("문제가뭐야?: "+ se)
        }
    }
    fun insertJoin(id: String, nick: String, pwd: String, addr: String, tel: String):Boolean{
        try {
            jiIsrtPstmt?.setString(1,id)
            jiIsrtPstmt?.setString(2,nick)
            jiIsrtPstmt?.setString(3,pwd)
            jiIsrtPstmt?.setString(4,addr)
            jiIsrtPstmt?.setString(5,tel)
            var i = jiIsrtPstmt!!.executeUpdate()
            if (i>0){
                println("성공")
                return true
            }else{
                println("실패")
                return false
            }
        }catch(se: SQLException){
            println("문제가뭐야?: "+se)
            return false
        }
    }
    fun selectId(id: String):Boolean{
        var rs: ResultSet? =null
        try {
            jiUidPstmt?.setString(1,id)
            var rs = jiUidPstmt?.executeQuery()
            if (rs!!.next()){
                return true
            }else{
                return false
            }
        }catch(se: SQLException){
            println("문제가뭐야?: "+ se)
            return false
        }finally {
            try {
                if (rs != null) rs.close()
            }catch(se: SQLException){}
        }
    }
    fun selectNick(nick: String): Boolean{
        var rs: ResultSet? =null
        try {
            jiUnickPstmt?.setString(1,nick)
            var rs = jiUnickPstmt?.executeQuery()
            if (rs!!.next()){
                return true
            }else{
                return false
            }
        }catch(se: SQLException){
            println("문제가뭐야?: "+ se)
            return false
        }finally {
            try {
                if (rs != null) rs.close()
            }catch(se: SQLException){}
        }
    }
    fun selectTel(Tel: String): Boolean{
        var rs: ResultSet? =null
        try {
            jiUtelPstmt?.setString(1,Tel)
            rs = jiUtelPstmt?.executeQuery()
            if (rs!!.next()){
                return true
            }else{
                return false
            }
        }catch(se: SQLException){
            println("문제가뭐야?: "+ se)
            return false
        }finally {
            try {
                if (rs != null) rs.close()
            }catch(se: SQLException){}
        }
    }
    fun closeAll(){
        try {
            jiIsrtPstmt?.close()
            jiUidPstmt?.close()
            jiUnickPstmt?.close()
            jiUtelPstmt?.close()
        }catch (se:SQLException){
            println("문제가뭐야?: "+ se)
        }
    }
}