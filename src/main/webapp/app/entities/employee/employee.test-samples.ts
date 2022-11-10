import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
  firstName: 'Donnell',
  phone: '816-479-6549 x42015',
};

export const sampleWithPartialData: IEmployee = {
  id: 47743,
  code: 'Implementation Pizza',
  firstName: 'Emily',
  isActive: true,
  phone: '(998) 469-5449 x933',
  city: 'Carson',
  salary: 63120,
};

export const sampleWithFullData: IEmployee = {
  id: 79174,
  code: 'bricks-and-clicks',
  firstName: 'Stanford',
  lastName: 'Leannon',
  email: 'Cayla.Muller44@gmail.com',
  isActive: false,
  phone: '1-629-854-7566',
  addressLine1: 'exuding',
  addressLine2: 'Health',
  city: 'Union City',
  country: 'Myanmar',
  salary: 3818,
};

export const sampleWithNewData: NewEmployee = {
  firstName: 'Missouri',
  phone: '1-916-412-9372 x6822',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
