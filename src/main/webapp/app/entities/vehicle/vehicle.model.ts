import { IMerchant } from 'app/entities/merchant/merchant.model';

export interface IVehicle {
  id: number;
  code?: string | null;
  name?: string | null;
  relatedUserLogin?: string | null;
  expenceCode?: string | null;
  isActive?: boolean | null;
  merchant?: Pick<IMerchant, 'id'> | null;
}

export type NewVehicle = Omit<IVehicle, 'id'> & { id: null };
