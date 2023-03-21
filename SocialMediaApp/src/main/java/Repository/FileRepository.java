package Repository;

import Domain.HasID;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public abstract class FileRepository<ID,E extends HasID<ID>> extends InMemoryRepository<ID,E>{
    private String Filename;

    public FileRepository(String Filename,Validator<E> validator) {
        super(validator);
        this.Filename=Filename;
        load_from_file();
    }

    private void load_from_file(){

        Path path = Paths.get(Filename);
        super.emptyRepo();
        try{
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line ->{
                E entity = extractEntity(Arrays.asList(line.split(";")));
                try {

                    super.add(entity);
                }catch(ValidationException ve){
                    System.out.println("File error");
                }
            });
        }catch(IOException ie){
            System.out.println("Io exception in repo");
        }



    }
    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E Entity);

    protected void append_to_file(E entity){

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(Filename,true))){
            bw.write(this.createEntityAsString(entity));
            bw.newLine();


        }catch(IOException ie){
            System.out.println("Write error");
        }
    }

    protected void write_to_file(){

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(Filename))){
            for(E entity : this.getAll()) {
                bw.write(this.createEntityAsString(entity));
                bw.newLine();
            }

        }catch(IOException ie){
            System.out.println("Write error");
        }
    }
    @Override
    public E add(E e)throws ValidationException{
        E e1 = super.add(e);
        if(e1 == null){
            return null;
        }
        append_to_file(e1);
        return e1;
    }

    @Override
    public E remove(ID id){
        E entity = super.remove(id);
        if(entity == null){
            return null;
        }
        else{
            write_to_file();
            return entity;
        }
    }
    @Override
    public E update(E entity) throws ValidationException{
        E e = super.update(entity);
        if(e != null){
            write_to_file();
            return e;
        }
        return null;
    }

    @Override
    public List<E> getAll() {
        //load_from_file();
        return super.getAll();
    }
}
