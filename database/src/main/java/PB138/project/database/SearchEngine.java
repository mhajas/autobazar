package PB138.project.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal on 21.5.2015.
 */
public class SearchEngine {

    List<String> conditions = new ArrayList<>();
    List<String> arguments = new ArrayList<>();

    public SearchEngine addCondition(String condition, String argument){
        if(condition == null || condition.isEmpty()){
            throw new IllegalArgumentException("condition is null or empty");
        }

        if(argument == null || argument.isEmpty()){
            throw new IllegalArgumentException("condition is null or empty");
        }

        condition.replace("{value}", "argument" + condition.length());

        conditions.add(condition);
        arguments.add(argument);
        return this;
    }

    public String getSerializedConditions(){
        String result = "";
        boolean start = true;
        for(String condition : conditions){
            if(!start) {
                result += " and ";
            }

            result += condition;
        }

        return result;
    }

    public String[] getArgumentsArray(){
        return arguments.toArray(new String[arguments.size()]);
    }
}
