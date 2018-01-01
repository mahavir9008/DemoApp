
package demo.payconiq.com.payconiq.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.annotations.PrimaryKey;

public class Repository  {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("forks")
    @Expose
    private Integer forks;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public Repository(Integer ids, String name, String names,Integer fork, String desc) {
        this.id = ids;
        this.name = name;
        this.fullName = names;
        this.forks = fork;
        this.description = desc;
    }


    public String getDescription() {
        return description;
    }


    public Integer getForks() {
        return forks;
    }

    public Repository(){

    }

}
