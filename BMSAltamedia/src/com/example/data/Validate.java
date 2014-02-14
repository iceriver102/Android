package com.example.data;

public class Validate {
	public boolean isEmail(String s){
        s=s.toLowerCase();
   //neu chuoi rong
   if(s=="")
      return false;
   //neu email la chuoi co khoang trang
   if(s.indexOf(" ")>0)
      return false;
   //neu email la chuoi khong co dau @
   if(s.indexOf("@")==-1)
      return false;
   var i=1;
   var sLength=s.length;
   //neu email la chuoi khong co dau .
   if(s.indexOf("..")!=-1)
      return false;
   //neu email la chuoi co 2 dau @
   if(s.indexOf("@")!=s.lastIndexOf("@"))
      return false;
   //neu email la chuoi co dau . cuoi cung
   if(s.lastIndexOf(".")==s.length-1)
      return false;
   //neu email la chuoi co ky  tu khong thuoc cac ky tu sau
   var str="abcdefghijklmnopqrstuvwxyz-@._0123456789";
   for(var j=0;j<s.length;j++){
      if(str.indexOf(s.charAt(j))==-1)
         return false;
   }
   //Neu email chuoi  du lieu hop le thi tra ve true.
   return true;
}
 
function isEmpty(s)
{
   return((s==null)||(s.length==0));
}
function isWhitespace(s)
{
   var whitespace="\t\n\r";
   var i;
   if(isEmpty(s))
      return true;
   for(i=0;i<s.length;i++)
   {
      var c=s.charAt(i);
      if(whitespace.indexOf(c)==-1)
         return false;
   }
   return true;
}
function isPhone(s){
        if(isEmpty(s))
      return false;
   //neu email la chuoi co khoang trang
   if(isWhitespace(s))
      return false;
        var str="+0123456789";
        for(var j=0;j<s.length;j++){
                if(str.indexOf(s.charAt(j))==-1)
                         return false;
        }
        if(s.charAt(0)!="+")
                if(s.length<10||s.length>11)
                        return false
        else if(s.length<10) return false;
        return true;
}

}
