import { IMerchant, NewMerchant } from './merchant.model';

export const sampleWithRequiredData: IMerchant = {
  id: 39951,
  code: 'AI Global',
  merchantSecret: 'state',
  name: 'functionalities',
  creditLimit: 2583,
  phone: '(956) 629-8511 x999',
  addressLine1: 'China Grocery copy',
  city: 'Arvelton',
  country: 'Mauritania',
  percentage: 67891,
  email: 'zrl.T@N1SxA.XD',
};

export const sampleWithPartialData: IMerchant = {
  id: 91338,
  code: 'Shoes deploy',
  merchantSecret: 'pixel Dominica alarm',
  name: 'Islands',
  creditLimit: 62285,
  phone: '742.722.1216',
  addressLine1: 'payment Nevada',
  addressLine2: 'Cliffs Bedfordshire',
  city: 'Collinsville',
  country: 'Serbia',
  percentage: 69001,
  creditScore: 56046,
  email: 'w<_@f.SY',
  isSandBox: false,
  storeDescription: 'pink extensible Future-proofed',
  storeSecondaryDescription: 'Up-sized Carolina',
};

export const sampleWithFullData: IMerchant = {
  id: 61127,
  code: 'deploy Gorgeous',
  merchantSecret: 'deposit Global',
  name: 'Generic SSL Madagascar',
  creditLimit: 73624,
  isActive: false,
  phone: '1-562-535-9815 x97718',
  addressLine1: 'Egypt Table',
  addressLine2: 'dot-com Massachusetts syndicate',
  city: 'Lake Sigmund',
  country: 'Afghanistan',
  percentage: 74342,
  creditScore: 29964,
  email: '_@&Ya06J._xJu',
  rating: 80644,
  leadTime: 65362,
  isSandBox: true,
  storeDescription: 'deposit Group Wooden',
  storeSecondaryDescription: 'Belize Courts Polarised',
};

export const sampleWithNewData: NewMerchant = {
  code: 'Electronics Producer Vatu',
  merchantSecret: 'synthesize Ball',
  name: 'Walk Ball',
  creditLimit: 20192,
  phone: '(818) 825-7247 x0818',
  addressLine1: 'withdrawal indigo',
  city: 'New Josiane',
  country: 'Djibouti',
  percentage: 54589,
  email: 't!@we[.`(',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
