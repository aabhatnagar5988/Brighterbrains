package brighterbrains.com.data;

/**
 * Created by DELL on 1/23/2016.
 */
public class ItemDataModel {

    public int _id;
    public  String
            Name="",
            Description="",
            Image="",
            Location="",
            Cost="";

    public ItemDataModel(int _id, String name, String description, String image, String location, String cost) {
        this._id = _id;
        Name = name;
        Description = description;
        Image = image;
        Location = location;
        Cost = cost;
    }
}
