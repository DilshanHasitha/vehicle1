export interface IEmployeeType {
  id: number;
  code?: string | null;
  name?: string | null;
  isActive?: boolean | null;
}

export type NewEmployeeType = Omit<IEmployeeType, 'id'> & { id: null };
