import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

public class SomeClipboardOwner implements ClipboardOwner {
    public void lostOwnership(Clipboard clip, Transferable trans ) {
        System.out.println( "Lost Clipboard Ownership" );
    }
}