package suite;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.ExcludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages(
{ "junit.control.mail", "junit.fxml" })
@ExcludePackages(
{ "junit.fxml.bdd", "junit.fxml.dialog", "junit.fxml.factory", "junit.fxml.node" })
public class TestSuite
{
}
