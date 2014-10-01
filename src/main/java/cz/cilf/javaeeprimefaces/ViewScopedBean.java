package cz.cilf.javaeeprimefaces;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ViewScopedBean implements Serializable {

    @PostConstruct
    private void init() {
        System.out.println("Constructing ViewScopedBean");
    }

    private int value = 0;

    public int getValue() {
        return value;
    }

    public void increaseValue() {
        value++;
    }

}
