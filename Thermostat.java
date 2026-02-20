package Project;

import java.util.Scanner;

public class Thermostat{
    public static void main(String[] args){
        while (true){
        Scanner input = new Scanner(System.in);
        int hour=0,usermenu,count=1,heaterstate,season;
        double targettemperature=22.0,noise;
        String difficulty="Medium",roomname="Room1";
        
        noise = (Math.random()*3-1.5);
        
        while (count==1) {
            System.out.println("Menu :"
                    + "\n 1-Change Room Name"
                    + "\n 2-Set Target Temperature(°C)"
                    + "\n 3-Set Difficulity/Insulation(Easy/Medium/Hard)"
                    + "\n 4-Start 1-Month Simulation(30 Days,720 Hours)"
                    + "\n 5-Exit");
            
            usermenu = input.nextInt();
            
            if (usermenu==1){
                input.nextLine();
                System.out.println("Enter the new room name : ");
                roomname = input.nextLine().toLowerCase();
            }
            else if (usermenu==2){
                System.out.println("Enter the new target temperature (C) : ");
                targettemperature = input.nextDouble();
            }
            else if (usermenu==3){
                input.nextLine();
                System.out.println("Enter the new difficulity/insulation level (Easy/Medium/Hard) : ");
                difficulty = input.nextLine().toLowerCase();
            }    
            else if (usermenu==4){
                System.out.println("The simulation will start with new variables : "
                        + "\n Room Name : "+roomname
                        + "\n Target Temperature : "+targettemperature
                        + "\n Difficulity/Insulation Level : "+difficulty);
                count=0;
            }
            else{
                System.out.println("System will close , have a nice day.");
                System.exit(0);
                break;
            }
        }
        
        //Temperature
        int averagetemperature=0,amplitude = 0,heateropentime=0;
        double outdoortemperature,outdoortemperaturesum=0,averageoutdoortemperature;
        double indoortemperature,indoortemperaturesum=0,averageindoortemperature;
        double minindoortemperature=1000,maxindoortemperature=0;
        //Temperature
        
        //Energy
        double energy,energysum=0;
        double basepower=1.0,a=0.20;
        //Energy
        
        //Comfort  Hours
        int comforthours=0;
        //Comfort Hours
        
        //Difficulty Presets
        double insulationlossfactor=0,heatergain=0;
        switch(difficulty.toLowerCase()){
            case "easy" -> {
                  insulationlossfactor = 0.05;
                  heatergain = 1.2;
            }
            case "medium" -> {
                  insulationlossfactor = 0.08;
                  heatergain = 1.0;
            }
            case "hard" -> {
                  insulationlossfactor = 0.12;
                  heatergain = 0.8;
            }
          }
        //Difficulty Presets
        
        //Season
        System.out.println("Choose the Season : "
                + "\n1-Winter"
                + "\n2-Spring"
                + "\n3-Autumm"
                + "\n4-Summer");
        
        season = input.nextInt();
        
        switch(season){
            case 1 -> {
                averagetemperature = 5;
                amplitude = 7;
            }
            case 2, 3 -> {
                averagetemperature = 15;
                amplitude = 6;
            }
            case 4 -> {
                averagetemperature = 28;
                amplitude = 5;
            }
        }
        
        //Simulation
        for(hour=0;hour<720;hour++){
            outdoortemperature = (averagetemperature+amplitude*Math.sin(2*Math.PI*hour/24))+noise;
            outdoortemperaturesum = outdoortemperature+outdoortemperaturesum;
            
            indoortemperature = outdoortemperature;
            
            if (indoortemperature<(targettemperature-0.3)){
                heaterstate = 1;
                indoortemperature = indoortemperature + insulationlossfactor*(outdoortemperature-indoortemperature)+heatergain*heaterstate;
                indoortemperaturesum = indoortemperature + indoortemperaturesum;
                heateropentime++;
                energy = basepower + a*Math.max(0,(targettemperature-indoortemperature));
                energysum = energy + energysum;
                if (Math.abs((indoortemperature-targettemperature))<=1.0){
                    comforthours++;
                }
                if (indoortemperature<minindoortemperature){
                    minindoortemperature=indoortemperature;
                }
                if (indoortemperature>maxindoortemperature){
                    maxindoortemperature=indoortemperature;
                }
            }
            else if (indoortemperature>(targettemperature+0.3)){
                heaterstate = 0;
                indoortemperature = indoortemperature + insulationlossfactor*(outdoortemperature-indoortemperature)+heatergain*heaterstate;
                indoortemperaturesum = indoortemperature+ indoortemperaturesum;
                energy = basepower + a*Math.max(0,0);
                energysum = energy + energysum;
                if (Math.abs((indoortemperature-targettemperature))<=1.0){
                    comforthours++;
                }
                if (indoortemperature<minindoortemperature){
                    minindoortemperature=indoortemperature;
                }
                if (indoortemperature>maxindoortemperature){
                    maxindoortemperature=indoortemperature;
                }
            }                           
        }
        
        averageoutdoortemperature = outdoortemperaturesum/720;
        averageindoortemperature = indoortemperaturesum/720;
        //Simulation
  
        //Output Settings
        System.out.printf(
            "***** Smart Thermostat Report *****%n"+
            "Room : %s%n"+
            "Target : %.1f °C     Difficulty : %s (k=%.2f,G=-%.1f)%n"+
            "Hours Simulated : 720%n"+
            "Heater On Hours : %d%n"+
            "Total Energy Used : %.2f kith%n"+
            "Indoor Temperature (°C) : %n"+
            "Min : %.2f     Max : %.2f%n"+
            "Comfort Hours (±1.0°C) : %d (%.2f%%)%n"+
            "Average Outdoor Temperature (°C) : %.2f%n%n"+
            "Simulation complete. Returning main menu..."+
            "%n(Pres Enter The Continue)"+
            "%n",
            roomname.toUpperCase(),
            targettemperature,difficulty,insulationlossfactor,heatergain,
            heateropentime,
            energysum,
            minindoortemperature,maxindoortemperature,
            comforthours,((double)comforthours*100/720),
            averageoutdoortemperature
        );
        
        input.nextLine();
        if (input.nextLine().equals(" ")){
            continue;
        }
        
        } 
    }
}
