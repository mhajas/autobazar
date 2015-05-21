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

        if(condition.substring(0, 2).equals("km")){
            condition = condition.replace("{value}", "number($argument" + conditions.size() + ")");
        }else {
            condition = condition.replace("{value}", "$argument" + conditions.size());
        }


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
            start = false;
        }

        return result;
    }

    public String[] getArgumentsArray(){
        return arguments.toArray(new String[arguments.size()]);
    }

    @Override
    public String toString() {
        return "SearchEngine{" +
                "conditions=" + conditions +
                ", arguments=" + arguments +
                '}';
    }
}
