import java.io.*;
import java.util.*;

public class FILE
{
    private File f;

    public FILE(String pathname)
    {
        f=new File(pathname);
    }
    
    public long getSize(){return f.length();}
    public long getDate(){return f.lastModified();}
    public String getName(){return f.getName();}
    public boolean isHidden(){return f.isHidden();} 
    public boolean isVisible(){return !f.isHidden();} 
    public boolean exists(){return f.isFile();}
    
    public byte[] getBytes(){return getBytes(false);};
    public byte[] getBytes(boolean gzip){return getBytes(0, this.getSize()-1, false);};
    public byte[] getBytes(long from, long to, boolean gzip){
        if( this.getSize()==0 )
            return null;
        if( !f.isFile() || f.isDirectory() || from>to || this.getSize()-1<to ){
            LOG.write("Failed to read file section! "+this.getName()+";from:"+from+";to:"+to+";", 700);
            return null;
        }
        
        byte[] out=null;
        try{
            FileReader fr = new FileReader(f);
            fr.skip(from);
            char[] cbuf=new char[((int) (to-from))]; //! MAX OF 2.147.483.647 Bytes!
            int red = fr.read(cbuf);
            
            fr.close();
        }catch ( FileNotFoundException e ){
                System.err.println( "Datei gibt’s nicht!" );
        }catch ( Exception e ){
                LOG.write("Failed to read file section! "+this.getName()+";from:"+from+";to:"+to+";", 700);
        }
        
        System.out.println("file to byte array! "+this.getName()+"; from:"+from+"; to:"+to+"; expect:"+(to-from)+"; real:"+out.length+";");
        
        if( out!=null && gzip )
            out=(new GZIP().compress(out));
        
        return out;
    }
}