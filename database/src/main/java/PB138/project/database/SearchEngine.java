package PB138.project.database;

import sun.tools.jar.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Michal on 21.5.2015.
 */
public class SearchEngine {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    List<String> conditions = new ArrayList<>();
    List<String> arguments = new ArrayList<>();

    public SearchEngine addCondition(String condition, String argument){
        log.log(Level.INFO, "Search Engine, condition: " + condition+ " and arguments: "+argument);
        if(condition == null || condition.isEmpty()){
            log.log(Level.SEVERE, "IllegalArgumentException in SearchEngine: condition is null or empty");
            throw new IllegalArgumentException("condition is null or empty");
        }

        if(argument == null || argument.isEmpty()){
            log.log(Level.SEVERE, "IllegalArgumentException in SearchEngine: condition is null or empty");
            throw new IllegalArgumentException("condition is null or empty");
        }

        if(condition.substring(0, 2).equals("km") || condition.substring(0,5).equals("price")){
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
