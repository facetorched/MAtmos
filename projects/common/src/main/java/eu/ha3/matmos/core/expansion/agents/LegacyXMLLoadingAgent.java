package eu.ha3.matmos.core.expansion.agents;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.ha3.matmos.core.Knowledge;
import eu.ha3.matmos.core.expansion.ExpansionIdentity;
import eu.ha3.matmos.serialisation.JsonExpansions_EngineDeserializer;
import eu.ha3.matmos.serialisation.LegacyXMLExpansions_Engine1;
import eu.ha3.matmos.serialisation.expansion.SerialRoot;

@Deprecated
public class LegacyXMLLoadingAgent implements LoadingAgent {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final File jsonOutput;

    public LegacyXMLLoadingAgent() {
        this(null);
    }

    public LegacyXMLLoadingAgent(File jsonOutput) {
        this.jsonOutput = jsonOutput;
    }

    @Override
    public Exception load(ExpansionIdentity identity, Knowledge knowledge) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

            Document document = documentBuilder.parse(identity.getPack().getInputStream(identity.getLocation()));

            SerialRoot root = new LegacyXMLExpansions_Engine1().loadXMLtoSerial(document);
            try {
                if (!jsonOutput.exists()) {
                    jsonOutput.createNewFile();
                }

                FileWriter write = new FileWriter(jsonOutput);
                write.append(gson.toJson(root));
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new JsonExpansions_EngineDeserializer().loadSerial(root, identity, knowledge);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }
}
