package ss_3.iastate.edu.vendme;

import android.os.Bundle;


/**
 * Displays data that is currently being held about the specific machine. Includes a picture,
 *    its contents, price, location, branc/type of machine, etc...
 * Created by Jared Danner on 3/16/2018.
 */

public class MachineDescription extends MachineSelection {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_description);

    }
}
