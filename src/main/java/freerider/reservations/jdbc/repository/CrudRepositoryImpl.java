package freerider.reservations.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


/**
 * None-public implementation class of the {@link CrudRepository} interface.
 * 
 * See for Javadoc:
 * - https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
 * 
 * See for original source code:
 * - https://github.com/spring-projects/spring-data-commons/blob/main/src/main/java/org/springframework/data/repository/CrudRepository.java
 */
class CrudRepositoryImpl<T,ID> implements CrudRepository<T,ID> {

    /*
     * Open database connection.
     */
    private final Connection dbcon;

    /*
     * Table associated with the repository.
     */
    private final String tableName;

    /*
     * Combined RowMapper and object factory
     */
    private final RowMapper rowMapper;


    public CrudRepositoryImpl(Connection dbcon, String tableName) { //}, Consumer<DBDataMapper> initializer) {
        this.dbcon = dbcon;
        this.tableName = tableName;
        this.rowMapper = RowMapper.getInstance(dbcon);
    }

    @Override
    public <S extends T> S save(S entity) {
        if(entity==null)
            throw new IllegalArgumentException("argument entity is null");
        // 
        var opt = rowMapper.save(entity, tableName);
        return opt.isPresent()? cast(opt.get()) : entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public Optional<T> findById(ID id) {
        if(id==null)
            throw new IllegalArgumentException("argument id is null");
        //
        try(PreparedStatement ps = dbcon.prepareStatement(
            String.format("SELECT * FROM %s WHERE ID=?", tableName)
        )) {
            IdMapper<ID> idMapper = castIdMapper(IdMapper.getInstance(tableName));
            idMapper.setStatement(1, ps, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rowMapper.create(rs, tableName).map(e -> cast(e));
            }
        } catch (SQLException e) { report(e); }
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public Iterable<T> findAll() {
        try(
            Statement stmt = dbcon.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s", tableName))
        ) {
            List<T> result = new ArrayList<>();
            while(rs.next()) {
                rowMapper.create(rs, tableName).ifPresent(e -> result.add(cast(e)));
            }
            return result;
        // 
        } catch (SQLException e) { report(e); }
        return List.of();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    @Override
    public void deleteById(ID id) {
        if(id==null)
            throw new IllegalArgumentException("argument id is null");
        //
        try(PreparedStatement ps = dbcon.prepareStatement(
            String.format("DELETE FROM %s WHERE ID=?", tableName)
        )) {
            IdMapper<ID> idMapper = castIdMapper(IdMapper.getInstance(tableName));
            idMapper.setStatement(1, ps, id);
            // 
            int numRowsDeleted = ps.executeUpdate();
            System.out.println(String.format("numRowsDeleted: %d", numRowsDeleted));
        // 
        } catch (SQLException e) { report(e); }
    }

    @Override
    public void delete(T entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        if(ids==null)
            throw new IllegalArgumentException("argument ids is null");
        //
        int len = ((Collection<?>) ids).size();
        //
        try(PreparedStatement ps = dbcon.prepareStatement(
            // DELETE FROM table WHERE id IN (value1, value2, ...);
            String.format("DELETE FROM %s WHERE ID in (%s)", tableName, ", ?".repeat(len).substring(2))
        )) {
            IdMapper<ID> idMapper = castIdMapper(IdMapper.getInstance(tableName));
            var idt = ids.iterator();
            for(int i=1; idt.hasNext(); i++) {
                var id = idt.next();
                if(id==null)
                    throw new IllegalArgumentException("one of the ids is null");
                // 
                idMapper.setStatement(i, ps, id);
            }
            int numRowsDeleted = ps.executeUpdate();
            System.out.println(String.format("numRowsDeleted: %d", numRowsDeleted));
        // 
        } catch (SQLException e) { report(e); }
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }


    /*
     * Private helper methods.
     */

    /**
     * Cast object to type {@code S}.
     * @param <S> type to cast object to
     * @param s object to cast
     * @return object cast
     */
    @SuppressWarnings("unchecked")
    private <S extends T> S cast(Object s) {
        return (S)s;
    }

    @SuppressWarnings("unchecked")
    IdMapper<ID> castIdMapper(IdMapper<?> idMapper) { return (IdMapper<ID>)idMapper; }

    /**
     * Common reporting of exceptions.
     * @param e exception to report
     */
    private void report(Throwable e) {
        System.out.println(e.getMessage());
        // e.printStackTrace();
    }
}