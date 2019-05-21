/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Bruger
 */
public class FileWrapper {

    public long fileSize;
    private final File file;
    private int numberOfCharacters = 0;

    public FileWrapper(File file) throws IOException {
        this.file = file;
        setMetaData();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.fileSize ^ (this.fileSize >>> 32));
        hash = 43 * hash + this.numberOfCharacters;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileWrapper other = (FileWrapper) obj;
        if (this.fileSize != other.fileSize) {
            return false;
        }
        if (this.numberOfCharacters != other.numberOfCharacters) {
            return false;
        }
        return true;
    }
    
    public String getFilePath()
    {
        return file.getPath();
    }
    
    public String getFileExtension()
    {
        int lastIndexOfDot = file.getPath().lastIndexOf(".");
        return file.getPath().substring(lastIndexOfDot);
    }

    public void setMetaData() throws IOException {

        // Set filesize
        BasicFileAttributes at = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        fileSize = at.size();
        // Set number of characters
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        int counter = 0;
        String data;
        while ((data = reader.readLine()) != null) {
            counter += data.length();
        }
        numberOfCharacters=counter;
    }
}