package eu.ha3.matmos.editor.filechooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Courtesy of http://geek.starbean.net/?p=275
 *
 * << http://stackoverflow.com/questions/8581215/jfilechooser-and-checking-for-overwrite
 */
@SuppressWarnings("serial")
public class JsonFileChooser extends JFileChooser {
    protected String dotlessExtension;

    public JsonFileChooser(File dir) {
        this(dir, "json");
    }

    private JsonFileChooser(File dir, String dotlessExtension) {
        super(dir);
        this.dotlessExtension = dotlessExtension;
        addChoosableFileFilter(new FileNameExtensionFilter(String.format("%1$s Jason file (*.%1$s)", dotlessExtension), dotlessExtension));
    }
}
