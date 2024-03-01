package org.jabref.gui.push;

import java.util.Map;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import org.jabref.gui.DialogService;
import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.icon.JabRefIcon;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.push.CitationCommandString;
import org.jabref.preferences.PreferencesService;
import org.jabref.preferences.PushToApplicationPreferences;
import org.jabref.preferences.ExternalApplicationsPreferences;
import org.jabref.preferences.JabRefPreferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.mockito.Answers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

import javafx.beans.property.MapProperty;
import java.util.Arrays;


class PushToTexShopTest {

    private PushToTexShop pushToTexShop;

    @BeforeEach
    public void setup() {

        // Mock the DialogService and PreferencesService
        DialogService dialogService = mock(DialogService.class, Answers.RETURNS_DEEP_STUBS);
        PreferencesService preferencesService = mock(PreferencesService.class);
        PushToApplicationPreferences pushToApplicationPreferences = mock(PushToApplicationPreferences.class);
        ExternalApplicationsPreferences externalApplicationsPreferences = mock(ExternalApplicationsPreferences.class);

        // Mock the return value for getCommandPaths()
        String TexShopClientPath = "/usr/bin/texshop"; // For linux OS tobe changed in Windows and Mac
        String displayName = "TeXShop";

        Map<String, String> commandPaths = Map.of(displayName, TexShopClientPath);
        ObservableMap<String, String> observableCommandPaths = FXCollections.observableMap(commandPaths);
        when(pushToApplicationPreferences.getCommandPaths()).thenReturn(new SimpleMapProperty<>(observableCommandPaths));
        when(preferencesService.getPushToApplicationPreferences()).thenReturn(pushToApplicationPreferences);

        //Mock the return value for getCiteCommand()
        CitationCommandString mockCiteCommand = mock(CitationCommandString.class);
        // when(mockCiteCommand.prefix()).thenReturn("");
        // when(mockCiteCommand.suffix()).thenReturn("");
        when(mockCiteCommand.prefix()).thenReturn("[");
        when(mockCiteCommand.delimiter()).thenReturn(",");
        when(mockCiteCommand.suffix()).thenReturn("]");

        when(externalApplicationsPreferences.getCiteCommand()).thenReturn(mockCiteCommand);

        // Mock the return value for getExternalApplicationsPreferences()
        when(preferencesService.getExternalApplicationsPreferences()).thenReturn(externalApplicationsPreferences);

        // Create a new instance of PushToTexShop
        pushToTexShop = new PushToTexShop(dialogService, preferencesService);

        // Verify that the command path is correctly set in the preferences
        MapProperty<String, String> actualCommandPaths = pushToApplicationPreferences.getCommandPaths();
        Map<String, String> actualMap = actualCommandPaths.get();
        String actualPath = actualMap.get(displayName);
        assertEquals(TexShopClientPath, actualPath);
    }

    /**
     * To verify that the PushToTexShop class correctly returns its designated display name.
     * The display name is used to identify the application in the GUI.
     * 
     * Method: getDisplayName()
     * 
     */
    @Test
    void testDisplayName() {
        // Test whether the display name is correct
        assertEquals("TeXShop", pushToTexShop.getDisplayName());
    }

    /**
     * To verify that the PushToTexShop class correctly returns the command line for TexShop.
     * The command line is used to execute the application from the command line.
     * 
     * Method: getCommandLine()
     * 
     */
    @Disabled("Disabled, Need to add the method to AbstractPushToApplication getCommandLine()") 
    @Test
    void testGetCommandLine() {
        String keyString = "TestKey";
        String[] expectedCommand = new String[] {"/usr/bin/texshop", "--insert-text", "TestKey"};

        // pushToTexShop.setCommandPath("/usr/bin/texshop"); // TO add the function
        
        String[] actualCommand = pushToTexShop.getCommandLine(keyString);

        assertArrayEquals(expectedCommand, actualCommand);
    }

    /**
     * This test run the external application to push the keys to be cited 
     * 
     * Method: pushEntries()
     * 
     * [Precondition]: TexShop should be running in the background
     */
    @Disabled("Disabled as it needs running TexShop in the background. Start TexShop with &")
    @Test
    void pushEntries() {
        pushToTexShop.pushEntries(null, null, "key1,key2");
    }

    /**
     * To verify that the PushToTexShop class correctly returns the tooltip for TexShop.
     * The tooltip is used to display a short description of the application in the GUI.
     * 
     * Method: getTooltip()
     * 
     */
    @Test
    void testGetTooltip() {
        assertEquals("Push entries to external application (TeXShop)", pushToTexShop.getTooltip());
    }

    /**
     * To verify that the PushToTexShop class correctly returns the Prefix for the citation command.
     * The prefix is used to identify the start of the citation command.
     * 
     * Method: getCitePrefix()
     * 
     */
    @Test
    void getCitePrefixReturnsExpectedPrefix() {
        assertEquals("[", pushToTexShop.getCitePrefix());
    }

    /**
     * To verify that the PushToTexShop class correctly returns the delimiter for the citation command.
     * The delimiter is used to separate the citation keys.
     * 
     * Method: getDelimiter()
     * 
     */
    @Test
    void getDelimiterReturnsExpectedDelimiter() {
        assertEquals(",", pushToTexShop.getDelimiter());
    }

    /**
     * To verify that the PushToTexShop class correctly returns the Suffix for the citation command.
     * The suffix is used to identify the end of the citation command.
     * 
     * Method: getCiteSuffix()
     * 
     */
    @Test
    void getCiteSuffixReturnsExpectedSuffix() {
        assertEquals("]", pushToTexShop.getCiteSuffix());
    }
}