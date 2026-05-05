function SearchBar({ search, onSearchChange }) {
  return (
    <div>
      <label htmlFor="search">Buscar libro</label>
      <input
        id="search"
        type="text"
        placeholder="Buscar por título..."
        value={search}
        onChange={(e) => onSearchChange(e.target.value)}
      />
    </div>
  )
}

export default SearchBar