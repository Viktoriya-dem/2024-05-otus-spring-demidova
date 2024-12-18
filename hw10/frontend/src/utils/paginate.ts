


export function paginate(
  items: object[],
  currentPage: number,
  pageSize: number
): any {
  const startIndex = (currentPage - 1) * pageSize;
  return [...items].splice(startIndex, pageSize);
}

export function pagesArray(pagesCount: number): number[] {
  let array = [];
  for (let i = 1; i <= pagesCount; i++) {
    array.push(i);
  }
  return array;
}