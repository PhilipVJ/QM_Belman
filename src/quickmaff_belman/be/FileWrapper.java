/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Bruger
 */
public class FileWrapper
{

    public long timeForCreation;
    public long fileSize;
    private final File file;

    public FileWrapper(File file) throws IOException
    {
        this.file = file;
        setMetaData();
        
    }

    public long getTime()
    {
        return timeForCreation;
    }

    public long getSize()
    {
        return fileSize;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = (int) (31 * hash + this.timeForCreation);
        hash = (int) (31 * hash + this.fileSize);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final FileWrapper other = (FileWrapper) obj;
        if (this.timeForCreation != other.timeForCreation)
        {
            return false;
        }
        if (this.fileSize != other.fileSize)
        {
            return false;
        }
        return true;
    }

    public void setMetaData() throws IOException
    {

        BasicFileAttributes at = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        timeForCreation = at.creationTime().to(TimeUnit.MINUTES);
        fileSize = at.size();

    }

}
