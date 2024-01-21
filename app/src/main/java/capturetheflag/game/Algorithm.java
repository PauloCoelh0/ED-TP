package capturetheflag.game;

import capturetheflag.exceptions.EmptyCollectionException;
import capturetheflag.structures.ArrayUnorderedList;
import capturetheflag.structures.Network;

import java.util.Iterator;

public class Algorithm {
    private Network<Integer> network;

    public Algorithm(Network<Integer> network) {
        this.network = network;
    }

    public Iterator<Integer> findShortestPath(int startVertex, int endVertex, ArrayUnorderedList<Integer> locationsToAvoid) throws EmptyCollectionException {
        return network.findShortestPath(startVertex, endVertex, locationsToAvoid, network);
    }
}