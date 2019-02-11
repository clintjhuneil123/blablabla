package com.example.imyasfinal;

public class ArtistPorfolio {
    private String Name;
    private String Image;
    private String Description;
    private String Price;
    private String ArtistID;

    public ArtistPorfolio() {
    }

    public ArtistPorfolio(String name, String image, String description, String price, String artistID) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        ArtistID = artistID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getArtistID() {
        return ArtistID;
    }

    public void setArtistID(String artistID) {
        ArtistID = artistID;
    }
}
