package FeeshTank;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Christopher
 * Date: 4/30/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
interface FeeshContainer {
    public ArrayList<Feesh> getFeeshList();

    public ArrayList<Feesh> getFeeshListExcluding(Feesh a);

    public boolean removeFeesh(Feesh toRemove);


    public void sendList();
    public void receiveList();



}
