package subsistemalogueo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import subsistemarrhh.Administrativo;
import subsistemarrhh.Empleado;
import subsistemarrhh.Medico;

/**
 * Clase JsonClinica (Versión Unificada)
 * Contiene la lógica de persistencia y las clases internas necesarias
 * para manejar Fechas y Polimorfismo.
 */
public class JsonClinica {
    
    private static final String RUTA_ARCHIVO = "clinica.json";

    public static void guardarClinica(Clinica clinica) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                // Usamos las clases internas estáticas
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) 
                .registerTypeAdapterFactory(new EmpleadoAdapterFactory())
                .create();

        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(clinica, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    public static Clinica cargarClinica() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return null;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapterFactory(new EmpleadoAdapterFactory())
                .create();

        try (FileReader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, Clinica.class);
        } catch (IOException e) {
            System.err.println("Error al leer JSON: " + e.getMessage());
            return null;
        }
    }

    // ========================================================================
    // CLASES INTERNAS (ADAPTADORES)
    // Se definen como 'private static' porque solo JsonClinica las necesita.
    // ========================================================================

    /**
     * Adaptador para manejar LocalDate (Fechas de Java 8+)
     */
    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(formatter));
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            String fechaTexto = in.nextString();
            if (fechaTexto == null || fechaTexto.isEmpty()) return null;
            return LocalDate.parse(fechaTexto, formatter);
        }
    }

    /**
     * Fábrica de Adaptadores para manejar Polimorfismo en Empleados.
     * Agrega una etiqueta "tipo" para saber si es Medico o Administrativo.
     */
    private static class EmpleadoAdapterFactory implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            // Solo actuamos si la clase es Empleado o hija de Empleado
            if (!Empleado.class.isAssignableFrom(type.getRawType())) {
                return null;
            }

            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                        return;
                    }

                    // Obtenemos el adaptador de la clase CONCRETA (Medico/Admin)
                    TypeAdapter<T> delegate = (TypeAdapter<T>) gson.getDelegateAdapter(EmpleadoAdapterFactory.this, TypeToken.get(value.getClass()));
                    JsonElement jsonElement = delegate.toJsonTree(value);

                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        // Insertamos la etiqueta discriminadora
                        jsonObject.addProperty("tipo", value.getClass().getSimpleName());
                        elementAdapter.write(out, jsonObject);
                    } else {
                        delegate.write(out, value);
                    }
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    JsonElement jsonElement = elementAdapter.read(in);
                    if (jsonElement.isJsonNull()) return null;

                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    
                    // Leemos la etiqueta y decidimos qué clase instanciar
                    if (jsonObject.has("tipo")) {
                        String tipo = jsonObject.get("tipo").getAsString();
                        Class<?> claseDestino;

                        switch (tipo) {
                            case "Medico": claseDestino = Medico.class; break;
                            case "Administrativo": claseDestino = Administrativo.class; break;
                            default: claseDestino = Empleado.class; break;
                        }
                        
                        return (T) gson.getDelegateAdapter(EmpleadoAdapterFactory.this, TypeToken.get(claseDestino)).fromJsonTree(jsonElement);
                    }
                    throw new IOException("JSON de empleado corrupto: falta campo 'tipo'");
                }
            };
        }
    }
}