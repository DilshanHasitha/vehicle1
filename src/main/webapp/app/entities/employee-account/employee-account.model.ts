import dayjs from 'dayjs/esm';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IEmployeeAccount {
  id: number;
  transactionDate?: dayjs.Dayjs | null;
  transactionDescription?: string | null;
  transactionAmountDR?: number | null;
  transactionAmountCR?: number | null;
  transactionBalance?: number | null;
  transactionType?: Pick<ITransactionType, 'id'> | null;
  merchant?: Pick<IMerchant, 'id'> | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewEmployeeAccount = Omit<IEmployeeAccount, 'id'> & { id: null };
