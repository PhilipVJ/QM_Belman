/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

/**
 *
 * @author Philip
 */
public class FolderCheckResult {
    
    private final int numberOfNewlyAddedFiles;
    private final int numberOfCorruptFiles;

    public FolderCheckResult(int numberOfNewlyAddedFiles, int numberOfCorruptFiles) {
        this.numberOfNewlyAddedFiles = numberOfNewlyAddedFiles;
        this.numberOfCorruptFiles = numberOfCorruptFiles;
    }

    public int getNumberOfNewlyAddedFiles() {
        return numberOfNewlyAddedFiles;
    }

    public int getNumberOfCorruptFiles() {
        return numberOfCorruptFiles;
    }   
}
