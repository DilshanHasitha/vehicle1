import { IUser } from 'app/entities/user/user.model';
import { IEmployeeType } from 'app/entities/employee-type/employee-type.model';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';

export interface IEmployee {
  id: number;
  code?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  isActive?: boolean | null;
  phone?: string | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  city?: string | null;
  country?: string | null;
  salary?: number | null;
  user?: Pick<IUser, 'id'> | null;
  type?: Pick<IEmployeeType, 'id'> | null;
  vehicles?: Pick<IVehicle, 'id'>[] | null;
  merchant?: Pick<IMerchant, 'id'> | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
