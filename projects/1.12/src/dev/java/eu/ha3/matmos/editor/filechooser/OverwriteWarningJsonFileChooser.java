package eu.ha3.matmos.editor.filechooser;

import java.io.File;

import javax.swing.JOptionPane;

/**
 * Courtesy of http://geek.starbean.net/?p=275
 *
 * << http://stackoverflow.com/questions/8581215/jfilechooser-and-checking-for-overwrite
 */
@SuppressWarnings("serial")
public class OverwriteWarningJsonFileChooser extends JsonFileChooser {
    public OverwriteWarningJsonFileChooser(File dir) {
        super(dir);
    }

    @Override
    public File getSelectedFile() {
        File selectedFile = super.getSelectedFile();

        if (selectedFile != null) {
            String name = selectedFile.getName();
            if (!name.contains(".")) {
                selectedFile = new File(selectedFile.getParentFile(), name + '.' + dotlessExtension);
            }
        }

        return selectedFile;
    }

    @Override
    public void approveSelection() {
        if (getDialogType() == SAVE_DIALOG) {
            File selectedFile = getSelectedFile();
            if (selectedFile != null && selectedFile.exists()) {
                int response = JOptionPane.showConfirmDialog(this, "The file " + selectedFile.getName() + " already exists. Do you want to replace the existing file?", "Ovewrite file", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }

        super.approveSelection();
    }
}
