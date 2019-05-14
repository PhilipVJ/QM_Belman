/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickmaff_belman.be;

import quickmaff_belman.gui.model.WorkerFilterOption;

/**
 *
 * @author Philip
 */
public class Filter
{

    private final WorkerFilterOption wFilter;
    private final String searchWord;

    public Filter(WorkerFilterOption filterOption)
    {
        this.wFilter = filterOption;
        searchWord = "";
    }

    public Filter(WorkerFilterOption wFilter, String searchWord)
    {
        this.wFilter = wFilter;
        this.searchWord = searchWord;
    }

    public WorkerFilterOption getwFilter()
    {
        return wFilter;
    }

    public String getSearchWord()
    {
        return searchWord;
    }

    public boolean validBoardTask(BoardTask task)
    {
        boolean containsSearchWord = false;
        boolean matchesWorkFilter = false;
        
        if (task.toString().contains(searchWord))
        {
            containsSearchWord = true;
        }

        switch (wFilter)
        {
            case ACTIVEWORKERS:
                if (task.getActiveWorker() != null)
                {
                    matchesWorkFilter = true;
                }
                break;

            case NONACTIVEWORKERS:
                if (task.getActiveWorker() == null)
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
