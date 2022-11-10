import { IExUser, NewExUser } from './ex-user.model';

export const sampleWithRequiredData: IExUser = {
  id: 92426,
  login: 'Keyboard grid-enabled',
  firstName: 'Heber',
  email: 'j=n{:j@wL.5FUj_',
  phone: '802.716.5082',
  addressLine1: 'Object-based Steel Plastic',
  city: 'Normal',
  country: 'Syrian Arab Republic',
  userLimit: 67589,
};

export const sampleWithPartialData: IExUser = {
  id: 86591,
  login: 'Keyboard Granite',
  firstName: 'Emilie',
  email: '#M`@Q.R',
  isActive: true,
  phone: '(481) 658-2385 x33722',
  addressLine1: 'Shoals',
  city: 'North Alexandria',
  country: 'Venezuela',
  userLimit: 660,
};

export const sampleWithFullData: IExUser = {
  id: 75462,
  login: 'Colorado Tenge sensor',
  firstName: 'Christy',
  lastName: 'Jast',
  email: 'mJ5S@cq.M~Da',
  isActive: true,
  phone: '307-554-9126',
  addressLine1: 'technologies Tunnel Bedfordshire',
  addressLine2: 'Product',
  city: 'Bernieton',
  country: 'Netherlands Antilles',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  userLimit: 97525,
  creditScore: 11507,
};

export const sampleWithNewData: NewExUser = {
  login: 'United Credit Accountability',
  firstName: 'Marquise',
  email: '9@/&+7.pyI',
  phone: '1-481-586-5410 x72595',
  addressLine1: 'Chief vertical Innovative',
  city: 'Heathcoteburgh',
  country: 'British Indian Ocean Territory (Chagos Archipelago)',
  userLimit: 15098,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
