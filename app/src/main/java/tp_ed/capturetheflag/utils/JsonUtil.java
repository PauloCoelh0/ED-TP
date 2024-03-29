package tp_ed.capturetheflag.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import tp_ed.structures.ArrayUnorderedList;
import tp_ed.structures.Network;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtil {

    public static void exportNetworkToJson(Network<Integer> network, String filename) {
        ArrayUnorderedList<JSONObject> edgesArray = new ArrayUnorderedList<>();

        for (int i = 0; i < network.size(); i++) {
            for (int j = 0; j < network.size(); j++) {
                if (network.edgeExists(i, j)) {
                    double weight = network.getWeight(i, j);
                    JSONObject edgeObject = new JSONObject();
                    edgeObject.put("vertex1", i);
                    edgeObject.put("vertex2", j);
                    edgeObject.put("weight", weight);
                    edgesArray.addToRear(edgeObject);
                }
            }
        }

        try (FileWriter file = new FileWriter(filename)) {
            file.write(edgesArray.toString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Erro ao exportar o grafo: " + e.getMessage());
        }

    }

    public static Network<Integer> importNetworkFromJson(String filename) throws IOException {
        Network<Integer> network = new Network<>();

        try (FileReader fileReader = new FileReader(filename)) {
            JSONTokener tokener = new JSONTokener(fileReader);
            JSONArray edgesArray = new JSONArray(tokener);

            for (int i = 0; i < edgesArray.length(); i++) {
                JSONObject edgeObject = edgesArray.getJSONObject(i);
                int vertex1 = edgeObject.getInt("vertex1");
                int vertex2 = edgeObject.getInt("vertex2");

                if (!network.containsVertex(vertex1)) {
                    network.addVertex(vertex1);
                }
                if (!network.containsVertex(vertex2)) {
                    network.addVertex(vertex2);
                }

                double weight = edgeObject.getDouble("weight");
                network.addEdge(vertex1, vertex2, weight);
            }
        }
        return network;
    }
}
