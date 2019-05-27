/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import quickmaff_belman.gui.model.SortFilterOption;
import quickmaff_belman.gui.model.WorkerFilterOption;

/**
 *
 * @author Philip
 */
public class Filter
{

    private final WorkerFilterOption wFilter;
    private final String searchWord;
    private final SortFilterOption sortOption;

    public Filter(WorkerFilterOption filterOption, SortFilterOption sortOption)
    {
        this.wFilter = filterOption;
        searchWord = "";
        this.sortOption = sortOption;
    }

    public Filter(WorkerFilterOption wFilter, String searchWord, SortFilterOption sortOption)
    {
        this.wFilter = wFilter;
        this.searchWord = searchWord;
        this.sortOption = sortOption;
    }

    public WorkerFilterOption getwFilter()
    {
        return wFilter;
    }

    public String getSearchWord()
    {
        return searchWord;
    }
    
    public SortFilterOption getSortOption()
    {
        return sortOption;
    }

    public boolean validBoardTask(BoardTask task)
    {
        boolean containsSearchWord = false;
        boolean matchesWorkFilter = false;
        String lowerCaseString = task.toString().toLowerCase();
        String lowerCaseSearchWord = searchWord.toLowerCase();
        
        if (lowerCaseString.contains(lowerCaseSearchWord))
        {
            containsSearchWord = true;
        }

        switch (wFilter)
        {
            case ACTIVEWORKERS:
                if (task.getActiveWorkers() != null)
                {
                    matchesWorkFilter = true;
                }
                break;

            case NONACTIVEWORKERS:
                if (task.getActiveWorkers() == null)
                {
                    matchesWorkFilter = true;
                }
                break;

            case SHOWALL:
                matchesWorkFilter = true;
                break;
        }

        if (containsSearchWord == true && matchesWorkFilter == true)
        {
            return true;
        }
        return false;

    }

}
