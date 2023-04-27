import java.sql.*

class EditinfoKdbc {
    var con : Connection? = null
    var EdiSltNPstmt : PreparedStatement? = null
    var EdiUpdPstmt : PreparedStatement? = null
    var EdiSltTPstmt : PreparedStatement? = null
    var EdiSltNsql = "select USERNICK from USERINFO where USERNICK=? and USERID != ?"
    var EdiUpdsql = "update USERINFO set USERNICK=?,USERPW=?,USERADDR=REPLACE(REPLACE(REGEXP_REPLACE(?, '[0-9]'), '특별'), '광역'),USERTEL=? where USERID=?"
    var EdiSltTsql = "select USERTEL from USERINFO where USERTEL=? and USERID !=?"

    var tower : Tower? = null

    constructor(tower: Tower){
        this.tower = tower
        this.con = tower.con
        try{
            EdiSltNPstmt = con!!.prepareStatement(EdiSltNsql)
            EdiUpdPstmt = con!!.prepareStatement(EdiUpdsql)
            EdiSltTPstmt = con!!.prepareStatement(EdiSltTsql)
        }catch (cnfe: ClassNotFoundException){
            println("문제가뭐야?: " + cnfe)
        }catch (se: SQLException){
            println("문제가뭐야?: " + se)
        }
    }
    fun selectNick(currentNick: String, currentId:String):Boolean{
        var rs: ResultSet? =null
        try {
            EdiSltNPstmt?.setString(1,currentNick)
            EdiSltNPstmt?.setString(2,currentId)
            var rs = EdiSltNPstmt?.executeQuery()

            if (rs!!.next()){
                return true
            }else{
                return false
            }
        }catch (se: SQLException){
            println("문제가뭐야?: " + se)
            return false
        }finally {
            try {
                if (rs != null) rs.close()
            }catch(se: SQLException){}
        }
    }
    fun selectTel(currentTel: String,currentId: String):Boolean{
        var rs: ResultSet? =null
        try {
            EdiSltTPstmt?.setString(1,currentTel)
            EdiSltTPstmt?.setString(2,currentId)
            var rs = EdiSltTPstmt?.executeQuery()
            if (rs!!.next()){
                return true
            }else{
                return false
            }
        }catch (se: SQLException){
            println("문제가뭐야?: " + se)
            return false
        }finally {
            try {
                if (rs != null) rs.close()
            }catch(se: SQLException){}
        }
    }
    fun updateEditInfo(updateNick:String, updatePwd:String, updateAddr:String, updateTel:String, currentId: String):Boolean{
        try {
            EdiUpdPstmt?.setString(1,updateNick)
            EdiUpdPstmt?.setString(2,updatePwd)
            EdiUpdPstmt?.setString(3,updateAddr)
            EdiUpdPstmt?.setString(4,updateTel)
            EdiUpdPstmt?.setString(5,currentId)
            var i = EdiUpdPstmt!!.executeUpdate()
            if(i>0){
                println("성공")
                return true
            }else {
                println("실패")
                return false
            }
        }catch (se:SQLException){
            println("문제가뭐야?: "+ se)
            return false
        }
    }
    fun closeAll(){
        try {
            EdiSltNPstmt?.close()
            EdiUpdPstmt?.close()
            EdiSltTPstmt?.close()
        }catch (se:SQLException){
            println("문제가뭐야?: "+ se)
        }
    }
}

