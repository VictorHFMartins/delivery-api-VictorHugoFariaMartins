package com.deliverytech.delivery.domain.utils;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class CalcularFrete {

    // Raio médio da Terra em quilômetros
    private static final Double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calcula a distância entre dois pontos usando a fórmula de Haversine.
     */
    public BigDecimal calcularDistanciaKm(Double lat1, Double lon1, Double lat2, Double lon2) {

        Double dLat = Math.toRadians(lat2 - lat1);
        Double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        Double haversine = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        Double distancia = 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(haversine));

        return BigDecimal.valueOf(distancia);
    }

    /**
     * Calcula taxa de entrega com base na distância percorrida.
     *
     * Regras de exemplo: - Até 2km: taxa fixa 5.00 - De 2km até 5km: 1.50 por
     * km adicional - Acima de 5km: 2.50 por km adicional
     */
    public BigDecimal calcularTaxaEntrega(BigDecimal distanciaKm) {

        if (distanciaKm.compareTo(BigDecimal.valueOf(2)) <= 0) {
            return BigDecimal.valueOf(5.00);
        }

        if (distanciaKm.compareTo(BigDecimal.valueOf(5)) <= 0) {
            return BigDecimal.valueOf(5.00 + (distanciaKm.doubleValue() - 2) * 1.50);
        }

        return BigDecimal.valueOf(5.00 + (3 * 1.50) + ((distanciaKm.doubleValue() - 5) * 2.50));
    }

    /**
     * - Recebe coordenadas da origem (restaurante) - Recebe coordenadas do
     * destino (cliente) - Retorna a taxa final
     */
    public BigDecimal calcularTaxa(Double latRest, Double lonRest, Double latCli, Double lonCli) {

        BigDecimal distancia = calcularDistanciaKm(latRest, lonRest, latCli, lonCli);

        return calcularTaxaEntrega(distancia);
    }
}
