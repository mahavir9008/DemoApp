package demo.payconiq.com.payconiq.realmdao;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * DataItem Table
 */
public class DataItem extends RealmObject {

    @PrimaryKey
    private Integer id;
    private Integer forks;
    private String description;
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public Integer getForks() {
        return forks;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public DataItem(Integer ids, String name, String names, Integer fork, String desc) {
        this.id = ids;
        this.fullName = names;
        this.forks = fork;
        this.description = desc;
    }
    public DataItem(){

    }

}