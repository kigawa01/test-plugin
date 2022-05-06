package net.kigawa.testplugin;

import java.util.Scanner;

public class MCCalculation
{
    public static final int x = -718;
    public static final int z = -107;

    public static final int r = 128;

    public static void main(String[] args)
    {
        var scanner = new Scanner(System.in);
        while (true) {
            var line = scanner.nextLine().split(" ");

            switch (line[0]) {
                case "stop" -> {
                    return;
                }
                case "print" -> print();
                case "position" -> position(line);
                case "in" -> in(line);
            }
        }

    }

    private static void position(String[] line)
    {
        if (line.length != 2 || !line[1].matches("[+-]?\\d*(\\.\\d+)?")) {
            System.out.println("/position <number>");
            return;
        }
        var i = Integer.parseInt(line[1]);
        System.out.println(Math.sqrt((r * r) - (i * i)));
    }

    private static void print()
    {
        for (int i = (int) Math.ceil(r / Math.sqrt(2)); i <= 128; i++) {
            var sqrt = Math.ceil(Math.sqrt((r * r) - (i * i)));
            System.out.println((x + i) + " " + (int) (z + sqrt) + "\u001b[00;m"
                    + "\u001b[00;31m | " + (x + i) + " " + (int) (z - sqrt) + "\u001b[00;m"
                    + "\u001b[00;32m | " + (x - i) + " " + (int) (z + sqrt) + "\u001b[00;m"
                    + "\u001b[00;33m | " + (x - i) + " " + (int) (z - sqrt) + "\u001b[00;m"
                    + "\u001b[00;34m | " + (int) (x + sqrt) + " " + (z + i) + "\u001b[00;m"
                    + "\u001b[00;35m | " + (int) (x + sqrt) + " " + (z - i) + "\u001b[00;m"
                    + "\u001b[00;36m | " + (int) (x - sqrt) + " " + (z + i) + "\u001b[00;m"
                    + "\u001b[00;37m | " + (int) (x - sqrt) + " " + (z - i) + "\u001b[00;m"
            );
        }
    }

    private static void in(String[] positions)
    {
        if (positions[1].matches("[+-]?\\d*(\\.\\d+)?")
                && positions[2].matches("[+-]?\\d*(\\.\\d+)?")) {

            double px = Integer.parseInt(positions[1]);
            double pz = Integer.parseInt(positions[2]);

            double dx = x - px;
            double dz = z - pz;

            if (Math.sqrt((dx * dx) + (dz * dz)) > r) {
                System.out.println("\u001b[00;31m範囲外\u001b[00;m");
            } else {
                System.out.println("\u001b[00;32m範囲内\u001b[00;m");
            }
        }
    }
}

