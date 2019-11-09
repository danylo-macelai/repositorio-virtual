/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 22 de jul de 2019
 * version $
 */

import {
  Column,
  CreatedAt,
  DataType,
  Model,
  Table,
  UpdatedAt,
  BeforeUpdate,
  BeforeCreate,
  Length,
  AllowNull,
  Unique,
  Default,
} from 'sequelize-typescript';
import PerfilType from '../enums/PerfilType';
import { genSaltSync, hashSync, compareSync } from 'bcryptjs';

@Table({
  tableName: 'RV_USUARIO',
})
export class UsuarioModel extends Model<UsuarioModel> {
  @Length({
    min: 3,
    max: 120,
    msg: 'O nome informado deve estar entre 3 e 120 caracteres.',
  })
  @AllowNull(false)
  @Column({
    field: 'nome',
    type: DataType.STRING(120),
  })
  public nome!: string;

  @AllowNull(false)
  @Unique({
    msg: 'Já existe um usuário com o mesmo e-mail cadastrado.',
    name: 'email',
  })
  @Column({
    field: 'email',
    type: DataType.STRING(90),
  })
  public email!: string;

  @Length({
    min: 3,
    max: 70,
    msg: 'A senha informado deve estar entre 3 e 70 caracteres.',
  })
  @AllowNull(false)
  @Column({
    field: 'senha',
    type: DataType.STRING(70),
  })
  public senha!: string;

  @AllowNull(false)
  @Default(PerfilType.Padrao)
  @Column({
    field: 'perfil_type',
    type: DataType.ENUM(PerfilType.Padrao, PerfilType.Adminstrador),
  })
  public perfilType!: string;

  @AllowNull(false)
  @Default(true)
  @Column({
    field: 'ativo',
  })
  public ativo!: boolean;

  @AllowNull(false)
  @Default(false)
  @Column({
    field: 'bloqueado',
  })
  public bloqueado!: boolean;

  @AllowNull(false)
  @Default(false)
  @Column({
    field: 'alterar_senha',
  })
  public alterarSenha!: boolean;

  @AllowNull(false)
  @Default(0)
  @Column({
    field: 'tentativas_autenticacao',
  })
  public tentativasDeAutenticacao!: number;

  @AllowNull(false)
  @Default(DataType.NOW)
  @Column({
    field: 'ultima_troca_senha',
    type: DataType.DATE,
  })
  public ultimaTrocaSenha!: Date;

  @CreatedAt
  @AllowNull(false)
  @Column({
    field: 'criacao',
    type: DataType.DATE,
  })
  public criacao!: Date;

  @UpdatedAt
  @AllowNull(false)
  @Column({
    field: 'alteracao',
    type: DataType.DATE,
  })
  public alteracao!: Date;

  @BeforeCreate
  static onCreate(instance: UsuarioModel) {
    const salt = genSaltSync();
    instance.senha = hashSync(instance.senha, salt);
  }

  @BeforeUpdate
  static onUpdate(instance: UsuarioModel) {
    if (instance.changed('senha')) {
      const salt = genSaltSync();
      instance.senha = hashSync(instance.senha, salt);
    }
  }

  public verificarSenha = (encodedSenha: string, senha: string): boolean => {
    return compareSync(senha, encodedSenha);
  };
}
