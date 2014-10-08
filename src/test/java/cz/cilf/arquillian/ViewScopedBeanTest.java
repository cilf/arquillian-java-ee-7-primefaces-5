package cz.cilf.arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ViewScopedBeanTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Inject
    ViewScopedBean bean;

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentUrl;

    @FindBy(tagName = "h1")
    private WebElement h1;

    @FindBy(tagName = "h2")
    private WebElement h2;

    @FindBy(tagName = "button")
    private WebElement button;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        MavenResolverSystem resolver = Maven.resolver();
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "viewscopedbean.war")
                .setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"))
                .addClass(ViewScopedBean.class)
                .addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces:5.1").withoutTransitivity().asFile())
                .addAsWebResource(new File(WEBAPP_SRC, "index.xhtml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(webArchive.toString(true));
        return webArchive;
    }

    @Test
    public void shouldRenderIndexPage() {
        browser.get(deploymentUrl.toExternalForm());
        assertEquals("arquillian-java-ee-7-primefaces-5", h1.getText());
    }

    @Test
    public void shouldIncreaseValue() {
        browser.get(deploymentUrl.toExternalForm());
        assertEquals("Value: 0", h2.getText());
        guardAjax(button).click();
        assertEquals("Value: 1", h2.getText());
    }

}