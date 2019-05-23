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
    private final int numberOfDuplicates;
    private final int numberOfUnknownFiles;

    public FolderCheckResult(int numberOfNewlyAddedFiles, int numberOfCorruptFiles, int numberOfDuplicates, int numberOfUnknownFiles) {
        this.numberOfNewlyAddedFiles = numberOfNewlyAddedFiles;
        this.numberOfCorruptFiles = numberOfCorruptFiles;
        this.numberOfDuplicates = numberOfDuplicates;
        this.numberOfUnknownFiles = numberOfUnknownFiles;
    }

    public int getNumberOfUnknownFiles() {
        return numberOfUnknownFiles;
    }

    public int getNumberOfNewlyAddedFiles() {
        return numberOfNewlyAddedFiles;
    }

    public int getNumberOfCorruptFiles() {
        return numberOfCorruptFiles;
    }   

    public int getNumberOfDuplicates() {
        return numberOfDuplicates;
    }
    
    
}
