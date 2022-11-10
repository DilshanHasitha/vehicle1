import dayjs from 'dayjs/esm';

import { IEmployeeAccount, NewEmployeeAccount } from './employee-account.model';

export const sampleWithRequiredData: IEmployeeAccount = {
  id: 60173,
  transactionDate: dayjs('2022-11-08'),
  transactionDescription: 'Brook',
  transactionAmountDR: 88266,
  transactionAmountCR: 51546,
  transactionBalance: 88181,
};

export const sampleWithPartialData: IEmployeeAccount = {
  id: 48357,
  transactionDate: dayjs('2022-11-08'),
  transactionDescription: 'visualize Small',
  transactionAmountDR: 77961,
  transactionAmountCR: 57239,
  transactionBalance: 90606,
};

export const sampleWithFullData: IEmployeeAccount = {
  id: 42504,
  transactionDate: dayjs('2022-11-08'),
  transactionDescription: 'Macedonia Specialist e-tailers',
  transactionAmountDR: 86863,
  transactionAmountCR: 41268,
  transactionBalance: 77351,
};

export const sampleWithNewData: NewEmployeeAccount = {
  transactionDate: dayjs('2022-11-08'),
  transactionDescription: 'Global Representative Dollar',
  transactionAmountDR: 58172,
  transactionAmountCR: 23505,
  transactionBalance: 94693,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
