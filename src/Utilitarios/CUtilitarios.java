/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilitarios;

import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author rocio
 */
public class CUtilitarios {
        //****************** Atributos ************
    private static Scanner entrada = new Scanner(System.in);

    //*******************Metodos************
    public static int sol_entero(String msj) {
        int numero = -1;
        boolean error = true;
        do {
            try {
                System.out.println(msj);
                numero = entrada.nextInt();
                entrada.nextLine();
                error = false;
            } catch (Exception e) {
                //pendiente
                entrada.next();
            }
        } while (error);
        return numero;
    }

    public static float sol_decimal(String msj) {
        float numero = -1;
        boolean error = true;
        do {
            try {
                System.out.println(msj);
                numero = entrada.nextFloat();
                entrada.nextLine();
                error = false;
            } catch (Exception e) {
                //pendiente
                entrada.next();
            }
        } while (error);
        return numero;
    }

    public static String sol_cadena(String msj) {
        System.out.println(msj);
        return entrada.nextLine();
    }

    public static void msg(String msg, String origen) {
        JOptionPane.showMessageDialog(null,
                msg,
                origen,
                JOptionPane.PLAIN_MESSAGE);
    }
    
    public static void msg_correcto(String msg, String origen) {
        JOptionPane.showMessageDialog(null, msg, origen, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void msg_error(String msg, String origen) {
        JOptionPane.showMessageDialog(null,
                msg,
                origen,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void msg_adver(String msg, String origen) {
        JOptionPane.showMessageDialog(null,
                msg,
                origen,
                JOptionPane.WARNING_MESSAGE);
    }

}
