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


class PushToWinEdtTest {

    private PushToWinEdt pushToWinEdt;

    @BeforeEach
    public void setup() {

        // Mock the DialogService and PreferencesService
        DialogService dialogService = mock(DialogService.class, Answers.RETURNS_DEEP_STUBS);
        PreferencesService preferencesService = mock(PreferencesService.class);
        PushToApplicationPreferences pushToApplicationPreferences = mock(PushToApplicationPreferences.class);
        ExternalApplicationsPreferences externalApplicationsPreferences = mock(ExternalApplicationsPreferences.class);

        // Mock the return value for getCommandPaths()
        String WinEdtClientPath = "/usr/bin/winedt"; // For linux OS tobe changed in Windows and Mac
        String displayName = "WinEdt";

        Map<String, String> commandPaths = Map.of(displayName, WinEdtClientPath);
        ObservableMap<String, String> observableCommandPaths = FXCollections.observableMap(commandPaths);
        when(pushToApplicationPreferences.getCommandPaths()).thenReturn(new SimpleMapProperty<>(observableCommandPaths));
        when(preferencesService.getPushToApplicationPreferences()).thenReturn(pushToApplicationPreferences);

        //Mock the return value for getCiteCommand()
        CitationCommandString mockCiteCommand = mock(CitationCommandString.class);
        when(mockCiteCommand.prefix()).thenReturn("[");
        when(mockCiteCommand.delimiter()).thenReturn(",");
        when(mockCiteCommand.suffix()).thenReturn("]");

        when(externalApplicationsPreferences.getCiteCommand()).thenReturn(mockCiteCommand);

        // Mock the return value for getExternalApplicationsPreferences()
        when(preferencesService.getExternalApplicationsPreferences()).thenReturn(externalApplicationsPreferences);

        // Create a new instance of PushToWinEdt
        pushToWinEdt = new PushToWinEdt(dialogService, preferencesService);

        // Verify that the command path is correctly set in the preferences
        MapProperty<String, String> actualCommandPaths = pushToApplicationPreferences.getCommandPaths();
        Map<String, String> actualMap = actualCommandPaths.get();
        String actualPath = actualMap.get(displayName);
        assertEquals(WinEdtClientPath, actualPath);
    }

    /**
     * To verify that the PushToWinEdt class correctly returns its designated display name.
     * The display name is used to identify the application in the GUI.
     * 
     * Method: getDisplayName()
     * 
     */
    @Test
    void testDisplayName() {
        // Test whether the display name is correct
        assertEquals("WinEdt", pushToWinEdt.getDisplayName());
    }

    /**
     * To verify that the PushToWinEdt class correctly returns the command line for WinEdt.
     * The command line is used to execute the application from the command line.
     * 
     * Method: getCommandLine()
     * 
     */
    @Disabled("Disabled, Need to add the method to AbstractPushToApplication getCommandLine()") 
    @Test
    void testGetCommandLine() {
        String keyString = "TestKey";
        String[] expectedCommand = new String[] {"/usr/bin/winedt", "--insert-text", "TestKey"};
                
        String[] actualCommand = pushToWinEdt.getCommandLine(keyString);

        assertArrayEquals(expectedCommand, actualCommand);
    }

    /**
     * This test run the external application to push the keys to be cited 
     * 
     * Method: pushEntries()
     * 
     * [Precondition]: Vim should be running in the background
     */
    @Disabled("Disabled as it needs running WinEdt in the background. Start WinEdt with &")
    @Test
    void pushEntries() {
        pushToWinEdt.pushEntries(null, null, "key1,key2");
    }

    /**
     * To verify that the PushToWinEdtTest class correctly returns the tooltip for WinEdt.
     * The tooltip is used to display a short description of the application in the GUI.
     * 
     * Method: getTooltip()
     * 
     */
    @Test
    void testGetTooltip() {
        assertEquals("Push entries to external application (WinEdt)", pushToWinEdt.getTooltip());
    }

    /**
     * To verify that the PushToWinEdtTest class correctly returns the Prefix for the citation command.
     * The prefix is used to identify the start of the citation command.
     * 
     * Method: getCitePrefix()
     * 
     */
    @Test
    void getCitePrefixReturnsExpectedPrefix() {
        assertEquals("[", pushToWinEdt.getCitePrefix());
    }

    /**
     * To verify that the PushToWinEdt class correctly returns the delimiter for the citation command.
     * The delimiter is used to separate the citation keys.
     * 
     * Method: getDelimiter()
     * 
     */
    @Test
    void getDelimiterReturnsExpectedDelimiter() {
        assertEquals(",", pushToWinEdt.getDelimiter());
    }

    /**
     * To verify that the PushToTeXworks class correctly returns the Suffix for the citation command.
     * The suffix is used to identify the end of the citation command.
     * 
     * Method: getCiteSuffix()
     * 
     */
    @Test
    void getCiteSuffixReturnsExpectedSuffix() {
        assertEquals("]", pushToWinEdt.getCiteSuffix());
    }
}