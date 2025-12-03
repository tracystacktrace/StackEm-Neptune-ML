package net.tracystacktrace.stackem.neptune.container;

import net.tracystacktrace.stackem.neptune.category.EnumCategory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PreviewTexturePack extends ContainerTexturePack {
    public final String firstLine;
    public final String secondLine;
    public final String sha256;

    protected BufferedImage icon; //icon image
    protected int iconTextureID = -1; //for OpenGL

    protected String target_version;
    protected String website;
    protected String[] authors;
    protected String[] custom_categories;
    protected EnumCategory[] categories;

    private String[] bakedCategoriesList; //raw shit

    public PreviewTexturePack(
            File archiveFile,
            String name,
            String firstLine,
            String secondLine,
            String sha256
    ) {
        super(archiveFile, name);
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.sha256 = sha256;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void setStackemData(
            String target_version,
            String website,
            String[] authors,
            String[] custom_categories,
            String[] categories
    ) {
        this.target_version = target_version;
        this.website = website;
        this.authors = authors;
        this.custom_categories = custom_categories;

        // Custom boxing for EnumCategory
        if (categories != null && categories.length > 0) {
            final List<EnumCategory> rawData = new ArrayList<>();
            for (int i = 0; i < categories.length; i++) {
                final EnumCategory result = EnumCategory.define(categories[i]);
                if (result != null) {
                    rawData.add(result);
                }
            }
            this.categories = rawData.toArray(new EnumCategory[0]);
        }
    }

    /* Category Building Tools */

    public boolean hasWebsite() {
        return this.website != null && !this.website.isEmpty();
    }

    public boolean hasAuthors() {
        return this.authors != null && this.authors.length > 0;
    }

    public boolean hasBakedCategoriesList() {
        return this.bakedCategoriesList != null;
    }

    public String getWebsite() {
        return this.website;
    }

    public String [] getAuthors() {
        return this.authors;
    }

    public String [] getBakedCategories() {
        return this.bakedCategoriesList;
    }

    public void bakeCategoryList(Function<String, String> translateFunction) {
        if (this.categories != null || this.custom_categories != null) {
            this.bakedCategoriesList = EnumCategory.collect(translateFunction, this.categories, this.custom_categories);
        }
    }

    /* Icon Management Methods */

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public boolean hasIcon() {
        return this.icon != null;
    }

    public BufferedImage getIcon() {
        return this.icon;
    }

    public boolean hasTextureIndex() {
        return this.iconTextureID != -1;
    }

    public int getTextureIndex() {
        return this.iconTextureID;
    }

    public void setTextureIndex(int i) {
        this.iconTextureID = i;
    }

    public int popTextureIndex() {
        if (this.iconTextureID != -1) {
            int returnInt = this.iconTextureID;
            this.iconTextureID = -1;
            this.icon = null;
            return returnInt;
        }
        return this.iconTextureID;
    }
}
